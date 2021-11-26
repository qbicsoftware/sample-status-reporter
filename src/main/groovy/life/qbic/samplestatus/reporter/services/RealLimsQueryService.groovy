package life.qbic.samplestatus.reporter.services

import life.qbic.samplestatus.reporter.Sample
import life.qbic.samplestatus.reporter.api.LimsQueryService

import java.time.Instant

/**
 * <b>Class RealLimsQueryService</b>
 *
 * <p>Implementation of the {@link LimsQueryService} interface.
 * Enables the client to request sample information from the connected LIMS.
 * </p>
 *
 * @since 0.1.0
 */
class RealLimsQueryService implements LimsQueryService{
    /**
     * {@InheritDocs}
     */
    @Override
    List<Sample> getUpdatedSamples(Instant updatedSince) {
        return null
    }
}
