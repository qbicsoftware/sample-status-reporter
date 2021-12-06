package life.qbic.samplestatus.reporter

import spock.lang.Specification

import java.util.function.Function

/**
 * Tests for the {@link Result} class.
 *
 * @since 0.1.0
 */
class ResultSpec extends Specification {

    def "Given a value, the result should be ok and return the wrapped value"() {
        given:
        Result<String,Exception> stringResult = Result.of("My precious!")

        when:
        String value = stringResult.getValue()

        then:
        noExceptionThrown()
        assert stringResult.isOk()
        assert value == "My precious!"
    }

    def "Given no value, the attempt to access the contained value must throw a NoSuchElementException"() {
        given:
        Result<String, Exception> stringResult = Result.of(null)

        when:
        stringResult.getValue()

        then:
        thrown(NoSuchElementException.class)
    }

    def "Given an error has been produced during result creation, the result object must contain an exception object"() {
        given:
        Result<String, Exception> stringResult = Result.of(new RuntimeException("This went wrong!"))

        when:
        Exception e = stringResult.getError()

        then:
        noExceptionThrown()
        e instanceof RuntimeException
        stringResult.isError()
        !stringResult.isOk()
    }

    def "Mapping of a result of value A with a function that consumes A and produces B, must return a result of type B"(){
        given:
        Result<String, Exception> stringResult = Result.of("Awesome!")

        and:
        Function<String, Integer> ruler = ((String value) -> { return value.length()})

        when:
        Result<Integer, Exception> result = stringResult.map(ruler)

        then:
        result.isOk()
        result.getValue() instanceof Integer
        result.getValue() == 8
    }

    def "Test for result idiom with value"() {
        given:
        String actualValue = "A real value"
        Result<String, Exception> stringResult = Result.of(actualValue)

        and:
        ExecutionDummy<String> executionDummy = Mock(ExecutionDummy.class)

        when:
        switch (stringResult) {
            case { it.isOk() }: executionDummy.apply(stringResult.getValue()); break
            case { it.isError() }: throw stringResult.getError(); break
        }

        then:
        1 * executionDummy.apply(actualValue)
    }

    def "Test for result idiom with exception"() {
        given:
        String exceptionMessage = "Iuh, that went south!"
        Result<String, Exception> stringResult = Result.of(new RuntimeException(exceptionMessage))

        and:
        ExecutionDummy<String> executionDummy = new ExecutionDummy<>()

        when:
        switch (stringResult) {
            case { it.isOk() }: executionDummy.apply("Worked!"); break
            case { it.isError() }: throw new RuntimeException("Error present"); break
        }

        then:
        thrown(RuntimeException.class)
        stringResult.getError().getMessage() == exceptionMessage
    }

    def "Mapping a function to a result with value must the target result type with target value type and target value"() {
        given:
        // A six-char String
        String message = "Hello!"
        Result<String, Exception> stringResult = Result.of(message)

        and:
        Function<String, Integer> lengthCalculator = (s) -> { s.length() }

        when:
        Result<Integer, Exception> lengthResult = stringResult.map(lengthCalculator)

        then:
        lengthResult.isOk()
        lengthResult.getValue() == message.length() // 6
    }

    def "Mapping a function to a result with error must the target result type with target value type and input error"() {
        given:
        String message = "Failure!"
        Result<String, Exception> stringResult = Result.of(new RuntimeException(message))

        and:
        Function<String, Integer> lengthCalculator = (s) -> { s.length() }

        when:
        Result<Integer, Exception> lengthResult = stringResult.map(lengthCalculator)

        then:
        lengthResult.isError()
        lengthResult.getError().getMessage() == message // the initial error message
    }

    def "Given an exception in the mapped function passed to the result object, the final result object must contain this exception"() {
        given:
        String message = "We believe this might works..."
        Result<String, Exception> stringResult = Result.of(message)

        and:
        Function<String, Integer> lengthCalculator = (s) -> { s.notAvailableProperty }

        when:
        Result<Integer, Exception> lengthResult = stringResult.map(lengthCalculator)

        then:
        noExceptionThrown()
        lengthResult.isError()
        lengthResult.getError() instanceof MissingPropertyException // the wrapped exception
    }

    /**
     * Small helper class for mocking executions
     * @param <T>
     */
    class ExecutionDummy<T> implements Function<T,String> {
        @Override
        String apply(T t) {
            return "worked!"
        }
    }
}
