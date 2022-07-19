package life.qbic.samplestatus.reporter.services.utils

import life.qbic.samplestatus.reporter.Result

import java.util.function.Function

/**
 * <b>Class SampleStatusMapper<b>
 *
 * <p>Takes a String value and tries to map it to a known sample status.</p>
 *
 * @since 1.0.0
 */
class SampleStatusMapper implements Function<String, Result<String, Exception>> {

  enum SampleStatus {
    SAMPLE_RECEIVED("Sample received", "SAMPLE_RECEIVED"),
    SAMPLE_QC_PASSED("QC passed", "SAMPLE_QC_PASS"),
    SAMPLE_QC_FAILED("QC failed", "SAMPLE_QC_FAIL"),
    LIBRARY_PREP_FINISHED("Library completed", "LIBRARY_PREP_FINISHED")


    private final String limsStatus
    private final String qbicStatus

    private SampleStatus(String limsStatus, String qbicStatus) {
      this.limsStatus = limsStatus
      this.qbicStatus = qbicStatus
    }

    static Optional<SampleStatus> fromLimsStatus(String status) {
      return Arrays.stream(values())
              .filter(it -> it.limsStatus.equals(status))
              .findFirst()
    }

    static Optional<SampleStatus> fromQbicStatus(String status) {
      return Arrays.stream(values())
              .filter(it -> it.qbicStatus.equals(status))
              .findFirst()
    }
  }

  /**
   * <p>Tries to map a String value to a known sample status.</p>
   * @param s the String value you want to have mapped
   * @return the mapped String value
   */
  @Override
  Result<String, Exception> apply(String s) {
    return mapSampleStatus(s)
  }

  private Result<String, Exception> mapSampleStatus(String statusString) {
    if (statusString.isEmpty()) {
      return Result.of(new MappingException("Status value is empty."))
    }
    return SampleStatus.fromLimsStatus(statusString)
            .map(it -> Result.of(it.qbicStatus))
            .orElseGet(() -> Result.of(new MappingException("Cannot map unkown satus value: $statusString.")))
  }

  /**
   * <b>Class MappingException</b>
   * <p>Small mapping exception class that can be used when sample status mapping fails</p>
   */
  class MappingException extends RuntimeException {

    MappingException(String message) {
      super(message)
    }
  }
}
