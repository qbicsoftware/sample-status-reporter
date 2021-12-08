package life.qbic.samplestatus.reporter.api

import java.time.Instant

/**
 * <b>Interface UpdateSearchService</b>
 *
 * <p>Provides the time point of the last update search</p>
 *
 * @since 0.1.0
 */
interface UpdateSearchService {

    /**
     * <p>Returns the last update search time point.</p>
     * @return the last update search time point.
     *
     * @since 0.1.0
     */
    Instant getLastUpdateSearchTimePoint()

    /**
     * <p>Saves the last search time-point persistently.</p>
     * @param lastSearchTimePoint the time-point of the last search
     *
     * @since 0.1.0
     */
    void saveLastSearchTimePoint(Instant lastSearchTimePoint)
}
