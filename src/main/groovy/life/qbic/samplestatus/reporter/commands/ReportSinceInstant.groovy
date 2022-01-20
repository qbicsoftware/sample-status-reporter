package life.qbic.samplestatus.reporter.commands

import life.qbic.samplestatus.reporter.Result
import life.qbic.samplestatus.reporter.SampleStatusReporter
import life.qbic.samplestatus.reporter.SampleUpdate
import life.qbic.samplestatus.reporter.api.LimsQueryService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine

import java.time.Instant

/**
 * <b>Command that runs the app for a given instant</b>
 *
 * @since 1.0.0
 */
@CommandLine.Command(name = "SampleStatusReporter", version = "1.0.0", mixinStandardHelpOptions = true)
class ReportSinceInstant implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(ReportSinceInstant.class)

  @CommandLine.Option(names = ["-t", "--time-point"], description = "Point in time from where to search for updates e.g. 2022-01-01T00:00:00Z")
  Instant timePoint = Instant.now()

  private final LimsQueryService limsQueryService
  private final SampleStatusReporter statusReporter

  ReportSinceInstant(LimsQueryService limsQueryService, SampleStatusReporter statusReporter) {
    this.limsQueryService = limsQueryService
    this.statusReporter = statusReporter
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
    log.info("Gathering updated samples since $timePoint ...")
    List<Result<SampleUpdate, Exception>> updatedSamples = limsQueryService.getUpdatedSamples(getTimePoint())
    log.info("Found ${updatedSamples.size()} updated samples.")
    updatedSamples.stream()
            .filter(Result::isOk)
            .map(Result::getValue)
            .peek(it -> log.info("\tUpdating $it"))
            .forEach(statusReporter::reportSampleStatusUpdate)
    log.info("Finished processing.")
  }
}
