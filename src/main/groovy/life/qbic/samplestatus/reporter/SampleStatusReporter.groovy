package life.qbic.samplestatus.reporter

/**
 * <b>Reports sample updates to the ecosystem</b>
 *
 * <p>Classes implementing this interface will consume sample updates and propagate them to the persistence layer.</p>
 *
 * @since 1.0.0
 */
interface SampleStatusReporter {

  void reportSampleStatusUpdate(SampleUpdate sampleUpdate)

}
