package life.qbic.samplestatus.reporter


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

    @Override
    void reportSampleStatusUpdate(SampleUpdate sampleUpdate) {
        String sampleCode = sampleUpdate.getSample().getSampleCode()
        String status = sampleUpdate.getUpdatedStatus()
        Instant updateTimepoint = sampleUpdate.getModificationDate()
        sampleTrackingService.updateSampleStatus(sampleCode, status, updateTimepoint)
    }

}
