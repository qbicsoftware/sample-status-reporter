package life.qbic.samplestatus.reporter.api

/**
 * <b>Interface LocationService</b>
 *
 * <p>Provides the current location of the configured LIMS.</p>
 *
 * @since 1.0.0
 */
interface LocationService {

    /**
     * <p>Returns an {@link Optional} of the location the LIMS is configured for, determined by the user. <b>Must</b> return an
     * object of type {@link Optional#empty} if no matching location was found.</p>
     *
     * @return an {@link Optional} wrapping a matching current {@link Location} or empty, if none was found.
     * @throws ServiceException in case the service cannot retrieve the location due to for example connection issues.
     * Must not be thrown, if the internal query was successful but no matching location was found. Instead, return an {@link Optional#empty}.
     * @since 0.1.0
     */
    Optional<Location> getUpdatingPersonLocation()

}
