package life.qbic.samplestatus.reporter.services


import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import life.qbic.samplestatus.reporter.api.Address
import life.qbic.samplestatus.reporter.api.Location
import life.qbic.samplestatus.reporter.api.ResponsiblePerson
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

    private String locationEndpointPath

    @PostConstruct
    void initService() {
        locationEndpointPath = sampleTrackingBaseUrl + locationEndpoint
    }

    @Override
    Optional<Location> getLocationForUser(String userId) {
        URI requestURI = createUserLocationURI(userId)
        HttpResponse response = requestLocation(requestURI)
        return DtoMapper.parseLocationOfJson(response.body())
    }

    @Override
    void updateSampleLocation(String sampleCode, Location location, ResponsiblePerson responsiblePerson) {
        String locationJson = DtoMapper.createJsonFromLocation(location, responsiblePerson)
        URI requestUri = createSampleUpdateURI(sampleCode, locationJson)
    }

    private URI createUserLocationURI(String userId) {
        return URI.create("${this.locationEndpointPath}/${userId}")
    }

    private URI createSampleUpdateURI(String sampleCode, String locationJson) {
        return URI.create("${sampleTrackingBaseUrl}/${sampleCode}/currentLocation/${locationJson}")
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

    private static class DtoMapper {

        private static final LOCATION_NAME = "name"
        private static final LOCATION_CONTACT = "responsible_person"
        private static final LOCATION_CONTACT_EMAIL = "responsible_person_email"
        private static final LOCATION_ADDRESS = "address"
        private static final ADDRESS_AFFILIATION = "affiliation"
        private static final ADDRESS_STREET = "street"
        private static final ADDRESS_ZIP = "zip_code"
        private static final ADDRESS_COUNTRY = "country"

        protected static Optional<Location> parseLocationOfJson(String putativeLocationJson) {
            List<Map> locationMaps = parseJsonToList(putativeLocationJson )
            return locationMaps.stream().map(DtoMapper::convertMapToLocation).findFirst()
        }

        protected static String createJsonFromLocation(Location location, ResponsiblePerson responsiblePerson) {
            Map locationMap = convertLocationToMap(location, responsiblePerson)
            return JsonOutput.toJson(locationMap)
        }

        private static List<Map<?, ?>> parseJsonToList(String json) {
            return new JsonSlurper().parseText(json) as ArrayList<Map>
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

        /**
         * <pre>
         * {
         *     "name": "QBiC",
         *     "responsible_person": "Tobias Koch",
         *     "responsible_person_email": "tobias.koch@qbic.uni-tuebingen.de",
         *     "address": {
         *         "affiliation": "QBiC",
         *         "street": "Auf der Morgenstelle 10",
         *         "zip_code": 72076,
         *         "country": "Germany"
         *     }
         * }
         * </pre>
         * @param location
         * @return a map representing the location
         */
        private static Map<String, ?> convertLocationToMap(Location location, ResponsiblePerson responsiblePerson) {
            Map locationMap = [
                    "name": location.getLabel(),
                    "responsible_person": responsiblePerson.getFullName(),
                    "responsible_person_email": responsiblePerson.getEmail(),
                    "address": convertAddressToMap(location.getAddress())
            ]
            return locationMap
        }

        /**
         * <pre>
         * {
         *     "affiliation": "QBiC",
         *     "street": "Auf der Morgenstelle 10",
         *     "zip_code": 72076,
         *     "country": "Germany"
         * }
         * </pre>
         * @param address the address being converted to a map
         * @return a map containing information about the address provided
         */
        private static Map<String, String> convertAddressToMap(Address address) {
            Map addressMap = [
                    "affiliation": address.getAffiliation(),
                    "street": address.getStreet(),
                    "zip_code": address.getZipCode(),
                    "country": address.getCountry()
            ]
            return addressMap
        }
    }
}
