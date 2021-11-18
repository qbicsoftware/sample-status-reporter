package life.qbic.samplestatus.reporter.services

import groovy.json.JsonSlurper
import life.qbic.samplestatus.reporter.Location
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

/**
 * <class short description - 1 Line!>
 *
 * <More detailed description - When to use, what it solves, etc.>
 *
 * @since <version tag>
 */
@Component
@ConfigurationProperties
class QbicSampleTrackingService implements SampleTrackingService {

    @Value('${service.sampletracking.url}')
    private String sampleTrackingBaseUrl

    @Value('${service.sampletracking.location.endpoint}')
    private String locationEndpoint

    @Value('${service.sampletracking.auth.user}')
    private String serviceUser

    @Value('${service.sampletracking.auth.password}')
    private String servicePassword

    private String fullEndpointPath

    @PostConstruct
    void initService() {
        fullEndpointPath = sampleTrackingBaseUrl + locationEndpoint
    }

    @Override
    Location getLocationForUser(String userId) {
        URI requestURI = createURI(userId)
        println requestURI
        HttpResponse response = requestLocation(requestURI)
        return parseLocationOfJson(response.body())
    }

    private URI createURI(String userId) {
        return URI.create("${this.fullEndpointPath}/${userId}")
    }

    private HttpResponse requestLocation(URI requestURI) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(requestURI).build()
        println serviceUser
        println servicePassword
        HttpClient client = HttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(serviceUser, servicePassword.toCharArray())
                    }
                })
                .build()
        println client.send(request, HttpResponse.BodyHandlers.ofString())
        return null
    }

    private static Location parseLocationOfJson(String putativeLocationJson) {
        Map locationMap = new JsonSlurper().parseText(putativeLocationJson) as Map
        println locationMap
        return null
    }
}
