package life.qbic.samplestatus.reporter

import spock.lang.Specification

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
        Result<String> stringResult = Result.of("My precious!")

        when:
        String value = stringResult.getValue()

        then:
        noExceptionThrown()
        assert stringResult.isOk()
        assert value == "My precious!"
    }

    def "Given no value, the attempt to access the contained value must throw a NoSuchElementException"() {
        given:
        Result<String> stringResult = Result.of(null)

        when:
        stringResult.getValue()

        then:
        thrown(NoSuchElementException.class)
    }

    def "Given an error has been produced during result creation, the result object must contain an exception object"() {
        Result<String> stringResult = Result.of(new RuntimeException("This went wrong!"))

        when:
        Exception e = stringResult.getError()

        then:
        noExceptionThrown()
        e instanceof RuntimeException
        stringResult.hasError()
        !stringResult.isOk()
    }
}
