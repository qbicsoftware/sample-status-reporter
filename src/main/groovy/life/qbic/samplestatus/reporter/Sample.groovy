package life.qbic.samplestatus.reporter

import groovy.transform.EqualsAndHashCode

/**
 * <b>Value Class Sample</b>
 *
 * @since 1.0.0
 */
@EqualsAndHashCode
class Sample {

  String sampleCode

  Sample(String sampleCode) {
    this.sampleCode = sampleCode
  }

  @Override
  String toString() {
    return "$sampleCode"
  }
}
