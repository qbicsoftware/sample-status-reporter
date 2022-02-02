package life.qbic.samplestatus.reporter.commands

import life.qbic.samplestatus.reporter.Result
import life.qbic.samplestatus.reporter.Sample
import life.qbic.samplestatus.reporter.SampleStatusReporter
import life.qbic.samplestatus.reporter.SampleUpdate
import life.qbic.samplestatus.reporter.api.LimsQueryService
import life.qbic.samplestatus.reporter.api.UpdateSearchService
import spock.lang.Specification

import java.time.Instant

class ReportSinceInstantSpec extends Specification {

  ReportSinceInstant underTest
  LimsQueryService limsQueryService = Stub()
  SampleStatusReporter statusReporter = Mock()
  UpdateSearchService updateSearchService = Stub()

  def "when no samples were updated then no sample updates are triggered in the reporter"() {
    when: "no samples were updated"

    limsQueryService.getUpdatedSamples(_ as Instant) >> []
    updateSearchService.getLastUpdateSearchTimePoint() >> Optional.empty()

    underTest = new ReportSinceInstant(limsQueryService, statusReporter, updateSearchService)
    underTest.run()
    then: "no sample updates are triggered in the reporter"
    0 * statusReporter.reportSampleStatusUpdate(_)
  }

  def "given #n exceptions during sample update retrieval, when the reporter is run, then no updates are performed and the reporter fails"() {

    given: "#n exceptions during sample update retrieval"
    List<Result<SampleUpdate, Exception>> updates = []
    for (i in 0..<n) {
      updates.add(Result.of(new RuntimeException("test exception $i")))
    }
    // add one valid update
    Sample sample = new Sample(generateFakeSampleCode())
    SampleUpdate sampleUpdate = new SampleUpdate()
    sampleUpdate.setUpdatedStatus("DATA_AVAILABLE")
    sampleUpdate.setSample(sample)
    sampleUpdate.setModificationDate(Instant.now())
    updates.add(Result.of(sampleUpdate))

    limsQueryService.getUpdatedSamples(_ as Instant) >> updates
    updateSearchService.getLastUpdateSearchTimePoint() >> Optional.empty()

    when: "when the reporter is run"
    underTest = new ReportSinceInstant(limsQueryService, statusReporter, updateSearchService)
    underTest.run()

    then: "then no updates are performed and the reporter fails"
    0 * statusReporter.reportSampleStatusUpdate(_)
    thrown(RuntimeException)

    where:
    n << [1, 2, 3, 33, 102]

  }

  def "given #n random sample updates, when the reporter is run, then all sample updates are triggered in the reporter"() {
    given: "#n random sample updates"
    List<Result<SampleUpdate, Exception>> updates = []
    for (i in 0..<n) {
      Sample sample = new Sample(generateFakeSampleCode())
      SampleUpdate sampleUpdate = new SampleUpdate()
      sampleUpdate.setUpdatedStatus("DATA_AVAILABLE")
      sampleUpdate.setSample(sample)
      sampleUpdate.setModificationDate(Instant.now())
      updates.add(Result.of(sampleUpdate))
    }
    limsQueryService.getUpdatedSamples(_ as Instant) >> updates
    updateSearchService.getLastUpdateSearchTimePoint() >> Optional.empty()

    when: "the reporter is run"
    underTest = new ReportSinceInstant(limsQueryService, statusReporter, updateSearchService)
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
