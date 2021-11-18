package life.qbic.samplestatus.reporter.services

import life.qbic.samplestatus.reporter.Location

/**
 * <b><short description></b>
 *
 * <p><detailed description></p>
 *
 * @since <version tag>
 */
interface LocationService {

    Optional<Location> getCurrentLocation()

}