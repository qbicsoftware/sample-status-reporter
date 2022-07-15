package life.qbic.samplestatus.reporter.services

import life.qbic.samplestatus.reporter.api.Location
import life.qbic.samplestatus.reporter.api.Person

/**
 * <b><short description></b>
 *
 * <p><detailed description></p>
 *
 * @since <version tag>
 */
interface LocationService {

    /**
     * Provides the location of the user running the service, if one is known
     * @return the location if any could be determined
     */
    Optional<Location> getUpdatingPersonLocation()

}