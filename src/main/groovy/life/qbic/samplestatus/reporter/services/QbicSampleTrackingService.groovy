package life.qbic.samplestatus.reporter.services

import life.qbic.samplestatus.reporter.Location
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

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

    @PostConstruct
    void initService() {
        println "init service"
        println sampleTrackingBaseUrl + locationEndpoint
    }

    @Override
    Location getLocationForUser(String userId) {
        return null
    }
}
