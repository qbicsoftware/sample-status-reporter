package life.qbic.samplestatus.reporter

import spock.lang.Specification

import java.util.function.Function

/**
 * <class short description - 1 Line!>
 *
 * <More detailed description - When to use, what it solves, etc.>
 *
 * @since <version tag>
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
            case { it.isOk() }:
                executionDummy.execute(stringResult.getValue())
                break
            case { it.isError() }:
                throw stringResult.getError()
                break
        }

        then:
        1 * executionDummy.execute(actualValue)
    }

    def "Test for result idiom with exception"() {
        given:
        String exceptionMessage = "Iuh, that went south!"
        Result<String, Exception> stringResult = Result.of(new RuntimeException(exceptionMessage))

        and:
        ExecutionDummy<String> executionDummy = Mock(ExecutionDummy.class)

        when:
        switch (stringResult) {
            case { it.isOk() }:
                executionDummy.execute(stringResult.getValue())
                break
            case { it.isError() }:
                throw stringResult.getError()
                break
        }

        then:
        thrown(RuntimeException.class)
        stringResult.getError().getMessage() == exceptionMessage
    }

    class ExecutionDummy<T> {

        void execute(T t){}

    }
}
