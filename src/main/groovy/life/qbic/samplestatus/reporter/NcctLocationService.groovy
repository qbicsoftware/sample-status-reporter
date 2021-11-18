package life.qbic.samplestatus.reporter

/**
 * <b><short description></b>
 *
 * <p><detailed description></p>
 *
 * @since <version tag>
 */
class NcctLocationService implements LocationService {
    
    private String userId

    private SampleTrackingService sampleTrackingService
    
    NcctLocationService(String userId, SampleTrackingService sampleTrackingService) {
        this.userId = userId
        this.sampleTrackingService = sampleTrackingService
    }
    
    @Override
    Optional<Location> getCurrentLocation() {
        return Optional.ofNullable(sampleTrackingService.getLocationForUser(userId))
    }
}
