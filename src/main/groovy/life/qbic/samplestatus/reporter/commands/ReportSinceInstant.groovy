package life.qbic.samplestatus.reporter.commands

import life.qbic.samplestatus.reporter.Result
import life.qbic.samplestatus.reporter.SampleStatusReporter
import life.qbic.samplestatus.reporter.SampleUpdate
import life.qbic.samplestatus.reporter.api.LimsQueryService
import life.qbic.samplestatus.reporter.api.UpdateSearchService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors

/**
 * <b>Command that runs the app for a given instant</b>
 *
 * @since 1.0.0
 */
@CommandLine.Command(name = "SampleStatusReporter", version = "1.0.0", mixinStandardHelpOptions = true)
class ReportSinceInstant implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(ReportSinceInstant.class)

  @CommandLine.Option(names = ["-t", "--time-point"], description = "Point in time from where to search for updates e.g. '2022-01-01T00:00:00Z'.\nDefaults to the last successful run. \nIf never run successfully defaults to the same time yesterday.")
  final Instant timePoint

  private final LimsQueryService limsQueryService
  private final SampleStatusReporter statusReporter
  private final UpdateSearchService updateSearchService


  ReportSinceInstant(LimsQueryService limsQueryService, SampleStatusReporter statusReporter, UpdateSearchService updateSearchService) {
    this.limsQueryService = limsQueryService
    this.statusReporter = statusReporter
    this.updateSearchService = updateSearchService

    if (!timePoint) {
      timePoint = defaultTimePoint(updateSearchService)
    }
  }

  private static Instant defaultTimePoint(UpdateSearchService updateSearchService) {
    return updateSearchService.getLastUpdateSearchTimePoint().orElse(Instant.now().minus(1, ChronoUnit.DAYS))
  }


  /**
   * When an object implementing interface {@code Runnable} is used
   * to create a thread, starting the thread causes the object's
   * {@code run} method to be called in that separately executing
   * thread.
   * <p>
   * The general contract of the method {@code run} is that it may
   * take any action whatsoever.
   *
   * @see java.lang.Thread#run()
   */
  @Override
  void run() {
    Instant executionTime = Instant.now()
    List<Result<SampleUpdate, Exception>> updatedSamples
    try {
      log.info("Gathering updated samples since $timePoint ...")
      updatedSamples = limsQueryService.getUpdatedSamples(getTimePoint())
      log.info("Found ${updatedSamples.size()} updated samples.")
    } catch (Exception e) {
      throw new RuntimeException("Could not report sample updates successfully.", e)
    }

    def errors = updatedSamples.stream()
            .filter(Result::isError)
            .map(Result::getError).collect()
    if (errors.size() > 0) {
      def errorMessages = errors.stream().map(RuntimeException::getMessage).collect(Collectors.joining("\n\t"))
      errors.forEach((Exception e) -> log.debug(e.getMessage(), e))
      throw new RuntimeException("Encountered ${errors.size()} errors retrieving updated samples: \n\t$errorMessages")
    }

    try {
      updatedSamples.stream()
              .filter(Result::isOk)
              .map(Result::getValue)
              .peek(it -> log.info("\tUpdating $it"))
              .forEach(statusReporter::reportSampleStatusUpdate)
      log.info("Finished processing.")
    } catch (Exception e) {
      throw new RuntimeException("Could not report sample updates successfully.", e)
    }
    updateSearchService.saveLastSearchTimePoint(executionTime)
  }
}
