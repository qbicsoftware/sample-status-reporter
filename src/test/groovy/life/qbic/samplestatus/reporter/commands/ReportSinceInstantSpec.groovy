package life.qbic.samplestatus.reporter.commands

import life.qbic.samplestatus.reporter.SampleStatusReporter
import life.qbic.samplestatus.reporter.api.LimsQueryService
import spock.lang.Specification

class ReportSinceInstantSpec extends Specification {

  ReportSinceInstant underTest
  LimsQueryService limsQueryService
  SampleStatusReporter statusReporter

  def "when no samples were updated then no sample updates are triggered in the reporter"() {
    when: "no samples were updated"
    underTest = new ReportSinceInstant(limsQueryService, statusReporter)
    then: "no sample updates are triggered in the reporter"
  }
}
