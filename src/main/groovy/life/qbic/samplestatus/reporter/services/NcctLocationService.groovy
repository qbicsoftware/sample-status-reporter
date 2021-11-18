package life.qbic.samplestatus.reporter.services

import life.qbic.samplestatus.reporter.Location
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * <b><short description></b>
 *
 * <p><detailed description></p>
 *
 * @since <version tag>
 */
@ConfigurationProperties
class NcctLocationService implements LocationService {

    @Value('${}')
    private String userId

    private SampleTrackingService sampleTrackingService

    NcctLocationService(String userId, SampleTrackingService sampleTrackingService) {
        this.userId = userId
        this.sampleTrackingService = sampleTrackingService
    }

    @Override
    Optional<Location> getCurrentLocation() {
        return Optional.ofNullable(sampleTrackingService.getLocationForUser(userId))
    }
}
