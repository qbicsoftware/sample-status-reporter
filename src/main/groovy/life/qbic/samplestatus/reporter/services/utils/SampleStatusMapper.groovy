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

  enum KnownSampleStatus {
    SAMPLE_RECEIVED("Sample received", "SAMPLE_RECEIVED", false),
    SAMPLE_QC_PASSED("QC passed", "SAMPLE_QC_PASS", false),
    SAMPLE_QC_FAILED("QC failed", "SAMPLE_QC_FAIL", false),
    LIBRARY_PREP_FINISHED("Library completed", "LIBRARY_PREP_FINISHED", false),
    SEQUENCING_COMPLETED("Sequencing completed", "", true)


    private final String limsStatus
    private final String qbicStatus
    private final boolean ignored

    private KnownSampleStatus(String limsStatus, String qbicStatus, boolean ignored) {
      this.limsStatus = limsStatus
      this.qbicStatus = qbicStatus
      this.ignored = ignored
    }

    static Optional<KnownSampleStatus> fromLimsStatus(String status) {
      return Arrays.stream(values())
              .filter(it -> it.limsStatus.equals(status))
              .findFirst()
    }

    private boolean isIgnored() {
      return ignored
    }
  }

 /**
 * @param status the lims status to be checked
 * @return true if the status is marked as ignored; false otherwise
 */
  public static boolean isIgnoredLimsStatus(String status) {
    return KnownSampleStatus.fromLimsStatus(status)
            .map(KnownSampleStatus::isIgnored)
            .orElse(false)
  }

  /**
   * Maps lims to qbic sample status
   * @param limsStatus
   * @return a result containing the mapped value or the exception that occurred.
   */
  public Result<String, Exception> limsToQbicStatus(String limsStatus) {
    mapSampleStatus(limsStatus)
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
