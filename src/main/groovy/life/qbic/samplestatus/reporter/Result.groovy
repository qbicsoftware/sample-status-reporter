package life.qbic.samplestatus.reporter

/**
 * <class short description - 1 Line!>
 *
 * <More detailed description - When to use, what it solves, etc.>
 *
 * @since <version tag>
 */
class Result<S> {

    private final S data
    private final Exception exception

    static <S> Result<S> of(S value) {
        return new Result(value)
    }

    static <S> Result<S> of(Exception e) {
        return new Result(e)
    }

    private Result(S data) {
        this.data = data
        this.exception = null
    }

    private Result(Exception exception) {
        this.data = null
        this.exception = exception
    }

    S getValue() throws NoSuchElementException {
        if (!data) {
            throw new NoSuchElementException("Result with error has no value.")
        }
        return data
    }

    Exception getError() throws NoSuchElementException {
        if (!error) {
            throw new NoSuchElementException("Result with value has no error.")
        }
        return error
    }

    Boolean hasError() {
        return exception
    }

    Boolean isOk() {
        return data
    }
}
