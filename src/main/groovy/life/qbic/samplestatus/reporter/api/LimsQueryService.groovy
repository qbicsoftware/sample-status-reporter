package life.qbic.samplestatus.reporter.api

import life.qbic.samplestatus.reporter.Result
import life.qbic.samplestatus.reporter.SampleUpdate

import java.time.Instant

/**
 * <b>Interface LimsQueryService</b>
 *
 * <p>Provides access to a laboratory information system and enables clients to submit simple
 * sample queries.</p>
 *
 * @since 0.1.0
 */
interface LimsQueryService {

    /**
     * <p>Requests updated samples from a LIMS, that have been updated since a provided
     * time instant</p>
     *
     * <p> For example if you want to search for the updated samples since the last hour, you can just
     * define the instant method parameter like this:</p>
     *
     * <pre>
     *  Instant oneHourEarlier = Instant.now().minus(1, ChronoUnit.HOURS);
     * </pre>
     *
     * <p>Implementing classes need to return samples that have been updated
     * later or equal to the provided time-point.</p>
     *
     * @param updatedSince an instance that configures the query from which time-point in the past
     * updated samples shall be listed.
     * @return a list of updated samples
     * @since 0.1.0
     */
    List<Result<SampleUpdate, Exception>> getUpdatedSamples(Instant updatedSince)
}
