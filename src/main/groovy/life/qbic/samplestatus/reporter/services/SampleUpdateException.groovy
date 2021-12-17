package life.qbic.samplestatus.reporter.services

/**
 * <b>Exception to be thrown when a sample update failed</b>
 *
 * @since 1.0.0
 */
class SampleUpdateException extends RuntimeException {
    SampleUpdateException(String message) {
        super(message)
    }
}
