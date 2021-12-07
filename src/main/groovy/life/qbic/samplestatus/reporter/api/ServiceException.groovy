package life.qbic.samplestatus.reporter.api

/**
 * <b>Class ServiceException</b>
 *
 * <p>Shall be used, when unexpected exceptions occur during a service task execution, to indicate
 * to the client, that some action might be advised here.</p>
 *
 * @since 0.1.0
 */
class ServiceException extends RuntimeException {
    ServiceException() {
        super()
    }

    ServiceException(String message) {
        super(message)
    }
}
