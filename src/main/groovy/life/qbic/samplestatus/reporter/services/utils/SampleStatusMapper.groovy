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

  private static final String SAMPLE_RECEIVED = "SAMPLE_RECEIVED"
  private static final String SAMPLE_QC_PASS = "SAMPLE_QC_PASS"
  private static final String SAMPLE_QC_FAIL = "SAMPLE_QC_FAIL"
  private static final String LIBRARY_PREP_FINISHED = "LIBRARY_PREP_FINISHED"

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
    Result<String, Exception> result
    switch (statusString) {
      case "SAMPLE_RECEIVED":
        result = Result.of(SAMPLE_RECEIVED); break
      case "QC_PASSED":
        result = Result.of(SAMPLE_QC_PASS); break
      case "QC_FAILED":
        result = Result.of(SAMPLE_QC_FAIL); break
      case "LIBRARY_PREP_FINISHED":
        result = Result.of(LIBRARY_PREP_FINISHED); break
      default:
        result = Result.of(new MappingException("Cannot map unkown satus value: $statusString."))
    }
    return result
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


