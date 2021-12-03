package life.qbic.samplestatus.reporter

import java.util.function.Function

/**
 * <class short description - 1 Line!>
 *
 * <More detailed description - When to use, what it solves, etc.>
 *
 * @since <version tag>
 */
class Result<V, E extends Exception> {

    private final V value
    private final E exception

    static <V,E extends Exception> Result<V, E> of(V value) {
        return new Result<>(value)
    }

    static <V,E extends Exception> Result<V, E> of(E e) {
        return new Result<>(e)
    }

    private Result(V value) {
        this.value = value
        this.exception = null
    }

    private Result(E exception) {
        this.value = null
        this.exception = exception
    }

    V getValue() throws NoSuchElementException {
        if (!value) {
            throw new NoSuchElementException("Result with error has no value.")
        }
        return value
    }

    E getError() throws NoSuchElementException {
        if (!error) {
            throw new NoSuchElementException("Result with value has no error.")
        }
        return error
    }

    Boolean hasError() {
        return exception
    }

    Boolean isOk() {
        return value
    }

    def <U,E extends Exception> Result<U, E> map(Function<V, U> function) {
        Objects.requireNonNull(function)
        if (hasError()) {
            return new Result<U, E>(exception as E)
        } else {
            try {
                Result<U, E> result = new Result<>(function.apply(value))
                return result
            } catch (Exception e) {
                return new Result<U, E>(e as E)
            }
        }
    }
}
