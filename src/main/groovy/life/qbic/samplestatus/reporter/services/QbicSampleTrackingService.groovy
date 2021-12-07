package life.qbic.samplestatus.reporter.services

import groovy.json.JsonSlurper
import life.qbic.samplestatus.reporter.api.Address
import life.qbic.samplestatus.reporter.api.Location
import life.qbic.samplestatus.reporter.api.SampleTrackingService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

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

    private static final LOCATION_NAME = "name"
    private static final LOCATION_CONTACT = "responsible_person"
    private static final LOCATION_CONTACT_EMAIL = "responsible_person_email"
    private static final LOCATION_ADDRESS = "address"
    private static final ADDRESS_AFFILIATION = "affiliation"
    private static final ADDRESS_STREET = "street"
    private static final ADDRESS_ZIP = "zip_code"
    private static final ADDRESS_COUNTRY = "country"

    @PostConstruct
    void initService() {
        fullEndpointPath = sampleTrackingBaseUrl + locationEndpoint
    }

    @Override
    Optional<Location> getLocationForUser(String userId) {
        URI requestURI = createURI(userId)
        HttpResponse response = requestLocation(requestURI)
        return Optional.ofNullable(parseLocationOfJson(response.body()))
    }

    private URI createURI(String userId) {
        return URI.create("${this.fullEndpointPath}/${userId}")
    }

    private HttpResponse requestLocation(URI requestURI) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(requestURI).build()
        HttpClient client = HttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(serviceUser, servicePassword.toCharArray())
                    }
                }).build()
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    private static Location parseLocationOfJson(String putativeLocationJson) {
        if (putativeLocationJson == null || putativeLocationJson.isEmpty()) {
            return null
        }
        ArrayList<Map> locationMap = new JsonSlurper().parseText(putativeLocationJson) as ArrayList<Map>
        return convertMapToLocation(locationMap.get(0))
    }

    private static Location convertMapToLocation(Map locationMap) {
        Location location = new Location()
        location.label = locationMap.get(LOCATION_NAME) ?: ""
        location.contactPerson = locationMap.get(LOCATION_CONTACT) ?: ""
        location.contactEmail = locationMap.get(LOCATION_CONTACT_EMAIL) ?: ""
        location.address = convertMapToAddress(locationMap.get(LOCATION_ADDRESS) as Map)
        return location
    }

    private static Address convertMapToAddress(Map addressMap) {
        Address address = new Address()
        address.affiliation = addressMap.get(ADDRESS_AFFILIATION) ?: ""
        address.street = addressMap.get(ADDRESS_STREET) ?: ""
        address.zipCode = addressMap.get(ADDRESS_ZIP) ?: ""
        address.country = addressMap.get(ADDRESS_COUNTRY) ?: ""
        return address
    }
}
