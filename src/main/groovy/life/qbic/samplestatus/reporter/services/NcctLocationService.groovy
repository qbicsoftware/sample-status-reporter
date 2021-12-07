package life.qbic.samplestatus.reporter.services

import life.qbic.samplestatus.reporter.api.Location
import life.qbic.samplestatus.reporter.api.LocationService
import life.qbic.samplestatus.reporter.api.SampleTrackingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * <b><short description></b>
 *
 * <p><detailed description></p>
 *
 * @since <version tag>
 */
@Component
@ConfigurationProperties
class NcctLocationService implements LocationService {

    @Value('${service.sampletracking.location.user}')
    private String userId

    @Autowired
    private SampleTrackingService sampleTrackingService

    @Override
    Optional<Location> getCurrentLocation() {
        return sampleTrackingService.getLocationForUser(userId)
    }
}
