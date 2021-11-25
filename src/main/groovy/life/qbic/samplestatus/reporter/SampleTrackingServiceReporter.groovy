package life.qbic.samplestatus.reporter
/**
 * <b>Implementation of the {@link SampleStatusReporter} interface, that calls the QBiC sample
 * tracking REST service and submits sample status updates.</b>
 *
 * @since 0.1.0
 */
class SampleTrackingServiceReporter implements SampleStatusReporter {

    private URL sampleTrackingServiceUrl
    private URL sampleStatusUpdateEndpoint

    /**
     * <p>This constructor will create an instance that is able to call a HTTP REST service
     * given the REST service base URL and the service endpoint that will be used to submit the sample
     * status update.</p>
     * @param sampleTrackingServiceUrl the base URL of the sample tracking HTTP REST service
     * @param sampleStatusUpdateEndpoint the relative endpoint of the service, that will be called
     */
    SampleTrackingServiceReporter(URL sampleTrackingServiceUrl, URL sampleStatusUpdateEndpoint) {
        this.sampleTrackingServiceUrl = sampleTrackingServiceUrl
        this.sampleStatusUpdateEndpoint = sampleStatusUpdateEndpoint
    }

    /**
     * {@inheritDocs}
     * @param sampleUpdate
     */
    @Override
    void reportSampleStatusUpdate(SampleUpdate sampleUpdate) {
        def sampleDto = sampleUpdate.getSampleCode()
        def newStatus = sampleUpdate.getUpdatedStatus()
        postUpdateToRestService(sampleDto, newStatus)

    }


}
