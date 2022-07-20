package life.qbic.samplestatus.reporter.services.utils

import life.qbic.samplestatus.reporter.Result

/**
 * <b>Class SampleStatusMapper<b>
 *
 * <p>Takes a String value and tries to map it to a known sample status.</p>
 *
 * @since 1.0.0
 */
class SampleStatusMapper {

  private final enum KnownSampleStatus {
    SAMPLE_RECEIVED("Sample received", "SAMPLE_RECEIVED"),
    SAMPLE_QC_PASSED("QC passed", "SAMPLE_QC_PASS"),
    SAMPLE_QC_FAILED("QC failed", "SAMPLE_QC_FAIL"),
    LIBRARY_PREP_FINISHED("Library completed", "LIBRARY_PREP_FINISHED"),

    private final String limsStatus
    private final String qbicStatus

    private KnownSampleStatus(String limsStatus, String qbicStatus) {
      this.limsStatus = limsStatus
      this.qbicStatus = qbicStatus
    }

    private static Optional<KnownSampleStatus> fromLimsStatus(String status) {
      return Arrays.stream(values())
              .filter(it -> it.limsStatus.equals(status))
              .findFirst()
    }
  }

  /**
   * Maps lims to qbic sample status
   * @param limsStatus
   * @return a result containing the mapped value or the exception that occurred.
   */
  public Result<String, Exception> limsToQbicStatus(String limsStatus) {
    return mapSampleStatus(limsStatus)
  }

  private Result<String, Exception> mapSampleStatus(String statusString) {
    if (statusString == null) {
      return Result.of(new MappingException("Status value is null."))
    }
    if (statusString.isEmpty()) {
      return Result.of(new MappingException("Status value is empty."))
    }
    return Result.of(KnownSampleStatus.fromLimsStatus(statusString)
            .map(it -> it.qbicStatus)
            .orElseGet(() -> new MappingException("Cannot map unknown status value: $statusString.")))
  }

  /**
   * <b>Class MappingException</b>
   * <p>Small mapping exception class that can be used when sample status mapping fails</p>*/
  class MappingException extends RuntimeException {

    MappingException(String message) {
      super(message)
    }
  }
}
