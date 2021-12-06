package life.qbic.samplestatus.reporter

import life.qbic.samplestatus.reporter.services.SampleTrackingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * <b><short description></b>
 *
 * <p><detailed description></p>
 *
 * @since <version tag>
 */
@Component
class QbicSampleStatusReporter implements SampleStatusReporter {

    @Autowired
    private SampleTrackingService sampleTrackingService



    @Override
    void reportSampleStatusUpdate(SampleUpdate sampleUpdate) {

        // talk to sample tracking service
    }
}
