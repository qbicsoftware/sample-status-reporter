package life.qbic.samplestatus.reporter

import java.util.function.Function

/**
 * <b>Class Result</b>
 *
 * <p>This class introduces the Rust idiom to use Result objects to enforce the client code
 * to apply some best practises to safe value access and proper exception handling.</p>
 *
 * <p>Results can be used to wrap an actual value of type <code>V</code> or an exception of type
 * <code>E</code>. An object of type Result can only contain either a value or an exception, not both in the
 * same instance.</p>
 *
 * To properly deal with Result objects, a good idiom looks like this:
 *
 * <pre>
 *
 * final Result\\<String, Exception\> result = new Result("Contains actual information")
 * // access the value
 * switch (result) {
 *    case { it.isOk() }:
 *      doSomething(it.getValue())
 *      break
 *    case { it.isError() }:
 *      doErrorHandling(it.getError())
 *      break
 * }
 * </pre>
 *
 * @since 0.1.0
 */
class Result<V, E extends Exception> {

    private final V value
    private final E err

    /**
     * Static constructor method for creating a result object instance of type <code>V,E</code>
     * wrapping an actual value <code>V</code>.
     * @param value the notorious value to get wrapped in a result object
     * @return a new result object instance
     */
    static <V,E extends Exception> Result<V, E> of(V value) {
        return new Result<>(value)
    }

    /**
     * Static constructor method for creating a result object instance of type <code>V,E</code>
     * wrapping an error <code>E</code>.
     * @param e the suspicious error to get wrapped in a result object
     * @return a new result object instance
     */
    static <V,E extends Exception> Result<V, E> of(E e) {
        return new Result<>(e)
    }

    private Result(V value) {
        this.value = value
        this.err = null
    }

    private Result(E exception) {
        this.value = null
        this.err = exception
    }

    /**
     * Access the wrapped value if present
     * @return the wrapped value
     * @throws NoSuchElementException if no value exists in the result object
     */
    V getValue() throws NoSuchElementException {
        if (!value) {
            throw new NoSuchElementException("Result with error has no value.")
        }
        return value
    }

    /**
     * Access the wrapped error if present
     * @return the wrapped exception
     * @throws NoSuchElementException if no error exists in the result object
     */
    E getError() throws NoSuchElementException {
        if (!err) {
            throw new NoSuchElementException("Result with value has no error.")
        }
        return err as E
    }

    /**
     * Returns <code>true</code>, if the result object contains an error. Is always the negation
     * of {@link Result#isOk()}.
     * <p>So <code>{@link Result#isError()} == !{@link Result#isOk()}</code></p>
     * @return true, if the result object has an error, else false
     */
    Boolean isError() {
        return err as boolean
    }

    /**
     * Returns <code>true</code>, if the result object contains an error. Is always the negation
     * of {@link Result#isError()}.
     * <p>So <code>{@link Result#isOk()} == !{@link Result#isError()}</code></p>
     * @return true, if the result object has a value, else false
    */
    Boolean isOk() {
        return value as boolean
    }

    /**
     * <p>Maps the current result object to a consumer function, that expects the same input
     * type <code>V</code> as the result's value type and produces a result object
     * of type <code>U,E</code>.</p>
     *
     * <p>If the current result contains an error (and therefore has no value), the created result object
     * will contain the error of type <code>E</code> of the input result object.
     * </p>
     *
     * @param function a function transforming data of type <code>V</code> to <code>U</code>
     * @return a new result object instance of type <code>U,E</code>
     */
    def <U,E extends Exception> Result<U, E> map(Function<V, U> function) {
        Objects.requireNonNull(function)
        if (isError()) {
            return new Result<U, E>(err as E)
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
