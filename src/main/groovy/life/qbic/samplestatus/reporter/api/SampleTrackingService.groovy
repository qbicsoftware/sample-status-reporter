package life.qbic.samplestatus.reporter.api

import life.qbic.samplestatus.reporter.api.Location

/**
 * <b>Provides access to the sample tracking persistence layer.</b>
 *
 * <p>This service provides fundamental access to sample-tracking persistence.
 * It allows to retrieve information and stores information in the system.</p>
 *
 * @since 1.0.0
 */
interface SampleTrackingService {

    /**
     * Retrieves the location associated with the userId provided.
     * @param userId an identifier for a user in the sample tracking system
     * @return the location that this user is associated with
     * @since 1.0.0
     */
    Optional<Location> getLocationForUser(String userId)


}