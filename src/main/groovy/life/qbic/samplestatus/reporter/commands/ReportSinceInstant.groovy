package life.qbic.samplestatus.reporter.commands

import life.qbic.samplestatus.reporter.Result
import life.qbic.samplestatus.reporter.SampleStatusReporter
import life.qbic.samplestatus.reporter.api.LimsQueryService
import life.qbic.samplestatus.reporter.api.Location
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine

import java.time.Instant

/**
 * <b>Command that runs the app for a given instant</b>
 *
 * @since 1.0.0
 */
@CommandLine.Command(name = "ASCIIArt", version = "ASCIIArt 1.0", mixinStandardHelpOptions = true)
class ReportSinceInstant implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(ReportSinceInstant.class)

  @CommandLine.Option(names = ["-t", "--time-point"], description = "Point in time from where to search for updates")
  Instant timePoint = Instant.now()

  private final Location currentLocation
  private final LimsQueryService limsQueryService
  private final SampleStatusReporter statusReporter

  ReportSinceInstant(Location currentLocation, LimsQueryService limsQueryService, SampleStatusReporter statusReporter) {
    this.currentLocation = currentLocation
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
    List updatedSamples = limsQueryService.getUpdatedSamples(getTimePoint())
    updatedSamples.stream()
            .filter(Result::isOk)
            .map(Result::getValue)
            .forEach(statusReporter::reportSampleStatusUpdate)
  }
}
