package life.qbic.samplestatus.reporter

import java.time.Instant

/**
 * <b><short description></b>
 *
 * <p><detailed description></p>
 *
 * @since <version tag>
 */
interface SampleStatusProvider {

    Collection<SampleUpdate> getSampleStatusUpdates(Instant from, Instant to)

}