package life.qbic.samplestatus.reporter.api

import life.qbic.samplestatus.reporter.services.SampleUpdateException

import java.time.Instant

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
     * Updates a sample's status and time since that status is valid
     * This information is stored on the persistence layer.
     * @param sampleCode the code of the sample changing status or location
     * @param status sample status to be set
     * @param timestamp time of the update
     * @throws SampleUpdateException in case the sample update was unsuccessful
     * @since 1.0.0
     */
    void updateSampleStatus(String sampleCode, String status, Instant timestamp) throws SampleUpdateException

}
