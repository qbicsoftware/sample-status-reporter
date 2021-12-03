package life.qbic.samplestatus.reporter

import java.util.function.Supplier

/**
 * <class short description - 1 Line!>
 *
 * <More detailed description - When to use, what it solves, etc.>
 *
 * @since <version tag>
 */
class Result<S> implements Supplier<S>{

    private final S data
    private final Exception exception

    Result(S data, Exception exception) {
        this.data = data
        this.exception = exception
    }

    @Override
    S get() {
        if(!data) {
            throw new NoSuchElementException("Result with error has no value.")
        }
        return data
    }

    Exception getError() {
        if(!error) {
            throw new NoSuchElementException("Result with value has no error.")
        }
        return error
    }

    Boolean isError() {
        return exception
    }

    Boolean isOk() {
        return data
    }

    S getOrElse(Supplier<S> otherSupplier) {
        return { isError() ? otherSupplier : data }.run()
    }

    public <X extends Throwable> S getOrThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (!data) {
            throw exceptionSupplier.get()
        }
        return data
    }
}
