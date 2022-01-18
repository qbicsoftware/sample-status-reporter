package life.qbic.samplestatus.reporter.commands

import life.qbic.samplestatus.reporter.Result
import life.qbic.samplestatus.reporter.Sample
import life.qbic.samplestatus.reporter.SampleStatusReporter
import life.qbic.samplestatus.reporter.SampleUpdate
import life.qbic.samplestatus.reporter.api.LimsQueryService
import spock.lang.Specification

import java.time.Instant

class ReportSinceInstantSpec extends Specification {

  ReportSinceInstant underTest
  LimsQueryService limsQueryService = Stub()
  SampleStatusReporter statusReporter = Mock()

  def "when no samples were updated then no sample updates are triggered in the reporter"() {
    when: "no samples were updated"

    limsQueryService.getUpdatedSamples(_ as Instant) >> []

    underTest = new ReportSinceInstant(limsQueryService, statusReporter)
    underTest.run()
    then: "no sample updates are triggered in the reporter"
    0 * statusReporter.reportSampleStatusUpdate(_)
  }

  def "given #n random sample updates, when the reporter is run, then all sample updates are triggered in the reporter"() {
    given: "#n random sample updates"
    List<Result<SampleUpdate, Exception>> updates = []
    for (i in 0..< n) {
      Sample sample = new Sample(generateFakeSampleCode())
      SampleUpdate sampleUpdate = new SampleUpdate()
      sampleUpdate.setUpdatedStatus("DATA_AVAILABLE")
      sampleUpdate.setSample(sample)
      sampleUpdate.setModificationDate(Instant.now())
      updates.add(Result.of(sampleUpdate))
    }
    limsQueryService.getUpdatedSamples(_ as Instant) >> updates
    when: "the reporter is run"
    underTest = new ReportSinceInstant(limsQueryService, statusReporter)
    underTest.run()

    then: "all sample updates are triggered in the reporter"
    n * statusReporter.reportSampleStatusUpdate(_)
    where:
    n << [1, 2, 3, 33, 102]
  }

  private static String generateFakeSampleCode() {
    String code = "QABCD"
    return code
  }
}
