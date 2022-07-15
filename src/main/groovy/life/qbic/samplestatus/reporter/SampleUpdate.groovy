package life.qbic.samplestatus.reporter

import groovy.transform.EqualsAndHashCode

import java.time.Instant

/**
 * <b>Value Class SampleUpdate</b>
 *
 * @since 1.0.0
 */
@EqualsAndHashCode
class SampleUpdate {
  Sample sample
  String updatedStatus
  Instant modificationDate

  @Override
  String toString() {
    return "{$sample, $updatedStatus, $modificationDate}"
  }
}
