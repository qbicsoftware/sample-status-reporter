package life.qbic.samplestatus.reporter.services

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.systemsx.cisd.common.spring.HttpInvokerUtils
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.search.SearchResult
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.fetchoptions.SampleFetchOptions
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.search.SampleSearchCriteria

import life.qbic.samplestatus.reporter.api.LimsQueryService
import life.qbic.samplestatus.reporter.SampleUpdate
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.time.Instant
import java.util.Date
import java.util.HashMap
import java.util.List
import java.util.Map

/**
 * <b>Class RealLimsQueryService</b>
 *
 * <p>Implementation of the {@link LimsQueryService} interface.
 * Enables the client to request sample information from the connected LIMS.
 * </p>
 *
 * @since 0.1.0
 */
@Component
@ConfigurationProperties
class RealLimsQueryService implements LimsQueryService {

    private IApplicationServerApi openBisApplicationServerApi

    private final String sessionToken

    /**
     * <b>Main configuration constructor</b>
     *
     * <p>This constructor is used by Spring to create an Singleton instance of the {@link RealLimsQueryService}.</p>
     * <p>Please note, that we need three constructor parameters, that must be configured in the applications property file.</p>
     *
     * <p>We have the application property <code>service.openbis.user</code> that maps to the parameter <code>openbisUser</code>,
     * then the property <code>service.openbis.password</code> that maps to the parameter <code>openbisPassword</code>
     * and finally the property <code>service.openbis.api-server-url</code> that maps to the parameter <code>applicationServerUrl</code>.</p>
     *
     * <p>We also let you configure the server timeout, which uses the application property <code>service.openbis.server-timeout</code>
     * and maps it to the parameter <code>serverTimeout</code>.
     *
     * @param openbisUser the openBIS user id
     * @param openbisPassword the openBIS user password
     * @param applicationServerUrl the openBIS application server url
     * @param serverTimeout the server connection timeout
     */
    RealLimsQueryService(@Value('${service.openbis.user}') String openbisUser,
                         @Value('${service.openbis.password}') String openbisPassword,
                         @Value('${service.openbis.api-server-url}') String applicationServerUrl,
                         @Value('${service.openbis.server-timeout}') Integer serverTimeout) {
        this.openBisApplicationServerApi = HttpInvokerUtils.createServiceStub(
                IApplicationServerApi.class,
                applicationServerUrl + IApplicationServerApi.SERVICE_URL, serverTimeout)
        sessionToken = this.openBisApplicationServerApi.login(openbisUser, openbisPassword)
    }

    /**
     * <p>This method is called by the Spring framework, after an instance of this class
     * has been created.</p>
     * <p>We just check, if the authentication against openBIS has been successful
     * which is the case, when there is a session token present.</p>
     */
    @PostConstruct
    void init() {
        if (!sessionToken) {
            throw new AuthenticationException("Authentication against LIMS service failed.")
        }
    }

    /**
     * {@InheritDocs}
     */
    @Override
    List<SampleUpdate> getUpdatedSamples(Instant updatedSince) {
        SampleSearchCriteria criteria = new SampleSearchCriteria()
        // we make sure that the barcode is set, otherwise the sample is of no interest to us
        criteria.withProperty("QBIC_BARCODE").thatContains("Q")
        // only fetch latest samples
        criteria.withModificationDate().thatIsLaterThanOrEqualTo(Date.from(updatedSince))
 
        // we need to fetch properties, as the sample status (and barcode) is contained therein
        SampleFetchOptions fetchOptions = new SampleFetchOptions()
        fetchOptions.withProperties()

        SearchResult<Sample> result = v3.searchSamples(sessionToken, criteria, fetchOptions)
        List<SampleUpdate> sampleUpdates = result.getObjects().stream().map( sample -> createSampleUpdate(sample) ).collect( Collectors.toList() )

        return sampleUpdates
    }

    public static SampleUpdate createSampleUpdate(Sample limsSample) {

        Map<String,String> properties = limsSample.getProperties()
        String sampleBarcode = properties.get("QBIC_BARCODE")

        life.qbic.samplestatus.reporter.Sample sample = new life.qbic.samplestatus.reporter.Sample(sampleBarcode)

        Date modificationDate = sample.getModificationDate()
        String updatedStatus = mapSampleStatus(properties.get("SAMPLE_STATUS"))

        return new SampleUpdate(sample = sample, updatedStatus = updatedStatus, modificationDate = modificationDate)
    }

    /**
     * This method does not make decisions based on which status should be updated. This decision might be made
     * elsewhere, as the default status 'METADATA_REGISTERED´ should probably not overwrite later, lab-independent statuses.
     */
    private String mapSampleStatus(String statusString) {
        switch (statusString) {
            case "SAMPLE_RECEIVED":
                return statusString
            case "QC_PASSED":
                return "SAMPLE_QC_PASS"
            case "QC_FAILED":
                return "SAMPLE_QC_FAIL"
            case "LIBRARY_PREP_FINISHED":
                return statusString
            default:
                return "METADATA_REGISTERED"
            }
    }

    /**
     * <b>Class AuthenticationException</b>
     *
     * <p>Small authentication exception class that can be used to indicate authentication exceptions</p>
     */
    class AuthenticationException extends RuntimeException {

        AuthenticationException(String message) {
            super(message)
        }
    }
}
