package life.qbic.samplestatus.reporter.api

import life.qbic.samplestatus.reporter.api.Location

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