package life.qbic.samplestatus.reporter.services

/**
 * <b>Exception to be thrown when parsing a data transfer object fails.</b>
 *
 * @since 1.0.0
 */
class DtoParseException extends RuntimeException {
    DtoParseException(String message) {
        super(message)
    }
}
