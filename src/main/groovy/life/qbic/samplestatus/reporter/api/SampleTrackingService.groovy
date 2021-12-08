package life.qbic.samplestatus.reporter.api

import life.qbic.samplestatus.reporter.services.SampleUpdateException

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
=======
    Optional<Location> getLocationForUser(String userId)
>>>>>>> 19690c18aee5fc51802bb21a2df0f89a9a046d6e:src/main/groovy/life/qbic/samplestatus/reporter/services/SampleTrackingService.groovy

    /**
     * Updates a sample to a given location with a status set in the location.
     * This information is stored on the persistence layer.
     * @param sampleCode the code of the sample changing status or location
     * @param location the new location
     * @throws SampleUpdateException in case the sample update was unsuccessful
     * @since 1.0.0
     */
    void updateSampleLocation(String sampleCode, Location location, Person responsiblePerson) throws SampleUpdateException


    /**
     * Updates a sample to a given location with a status set in the location.
     * This information is stored on the persistence layer.
     * @param sampleCode the code of the sample changing status or location
     * @param location the new location with a sample status already set
     * @param status sample status to be set.
     * @throws SampleUpdateException in case the sample update was unsuccessful
     * @since 1.0.0
     */
    void updateSampleLocation(String sampleCode, Location location, String status, Person responsiblePerson) throws SampleUpdateException
}