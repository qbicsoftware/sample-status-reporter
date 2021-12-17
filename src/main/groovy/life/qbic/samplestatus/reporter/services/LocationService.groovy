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
     * Provides the current location
     * @return the current location if any could be determined
     */
    Optional<Location> getCurrentLocation()

    /**
     * Provides the responsible users for the current location
     * @return user details of the responsible users
     */
    Optional<Person> getResponsiblePerson()
}