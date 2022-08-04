package life.qbic.samplestatus.reporter.services

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import life.qbic.samplestatus.reporter.api.SampleTrackingService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import static java.time.ZoneOffset.UTC

/**
 *
 * This service provides fundamental access to sample-tracking persistence. It allows to retrieve information and stores information in the system.
 *
 * @since 1.0.0
 */
@Component
@ConfigurationProperties
class QbicSampleTrackingService implements SampleTrackingService {

    @Value('${service.sampletracking.url}')
    private String sampleTrackingBaseUrl

    @Value('${service.sampletracking.endpoint}')
    private String endpoint

    @Value('${service.sampletracking.auth.user}')
    private String serviceUser

    @Value('${service.sampletracking.auth.password}')
    private String servicePassword

    private String endpointPath

    @PostConstruct
    void initService() {
        endpointPath = sampleTrackingBaseUrl + endpoint
    }

    private HttpResponse<String> requestSampleUpdate(URI requestURI, String statusJson) {
        HttpRequest request = HttpRequest
                .newBuilder(requestURI)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(statusJson))
                .build()
        HttpClient client = HttpClient.newBuilder()
                .authenticator(getAuthenticator())
                .build()
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    private URI createSampleUpdateURI(String sampleCode) {
        return URI.create("${sampleTrackingBaseUrl}/samples/${sampleCode}/status/")
    }

    private Authenticator getAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(serviceUser, servicePassword.toCharArray())
            }
        }
    }

    @Override
    void updateSampleStatus(String sampleCode, String status, Instant timestamp) throws SampleUpdateException {
        String statusJson = DtoMapper.createJsonFromStatus(status, timestamp)
        HttpResponse<String> response = requestSampleUpdate(createSampleUpdateURI(sampleCode), statusJson)
        if (response.statusCode() != 200) {
            throw new SampleUpdateException("Could not update $sampleCode to ${location.getLabel()} - ${response.statusCode()} : ${response.headers()}: ${response.body()}")
        }
    }

    private static class DtoMapper {

        protected static String createJsonFromStatus(String status, Instant arrivalTime) {
            Map parameters = new HashMap()
            parameters.add("status" : status)
            parameters.add("validFrom" : arrivalTime.toString())
            return JsonOutput.toJson(parameters)
        }

        private static String mapToDateTimeString(Instant timestamp) {
            // the pattern mentioned here is dictated by the data-model-lib location object
            var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'")
                    .withZone(ZoneId.from(UTC))
            return dateTimeFormatter.format(timestamp)
        }
    }
}
