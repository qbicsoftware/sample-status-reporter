package life.qbic.samplestatus.reporter.services

import life.qbic.samplestatus.reporter.api.Location
import life.qbic.samplestatus.reporter.api.LocationService
import life.qbic.samplestatus.reporter.api.Person
import life.qbic.samplestatus.reporter.api.SampleTrackingService
import life.qbic.samplestatus.reporter.api.ServiceException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * <b><short description></b>
 *
 * <p>Provides the current location of the configured LIMS. In this case the LIMS for the NCCT.</p>
 *
 * @since 1.0.0
 */
@Component
@ConfigurationProperties
class NcctLocationService implements LocationService {

    @Value('${service.sampletracking.location.user}')
    private String userId

    @Autowired
    private SampleTrackingService sampleTrackingService

    @Override
    Optional<Location> getUpdatingPersonLocation() throws ServiceException {
        return sampleTrackingService.getLocationForUser(userId)
    }

}
