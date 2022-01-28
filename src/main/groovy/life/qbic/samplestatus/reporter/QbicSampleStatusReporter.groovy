package life.qbic.samplestatus.reporter

import life.qbic.samplestatus.reporter.api.Location
import life.qbic.samplestatus.reporter.api.LocationService
import life.qbic.samplestatus.reporter.api.Person
import life.qbic.samplestatus.reporter.api.SampleTrackingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.Instant

/**
 * <b>Reports a sample update to the sample-tracking-service</b>
 * @since 1.0.0
 */
@Component
class QbicSampleStatusReporter implements SampleStatusReporter {

    @Autowired
    private SampleTrackingService sampleTrackingService

    @Autowired
    private LocationService locationService



    @Override
    void reportSampleStatusUpdate(SampleUpdate sampleUpdate) {
        Location currentLocation = locationService.getCurrentLocation().orElseThrow({
            new RuntimeException("No current location could be determined.")
        })
        String sampleCode = sampleUpdate.getSample().getSampleCode()
        String status = sampleUpdate.getUpdatedStatus()
        Instant updateTimepoint = sampleUpdate.getModificationDate()
        Person responsiblePerson = locationService.getResponsiblePerson().orElseThrow({
            new RuntimeException("No responsible person for the update was determined.")
        })
        sampleTrackingService.updateSampleLocation(sampleCode, currentLocation, status, updateTimepoint, responsiblePerson)
    }
}
