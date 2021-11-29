package life.qbic.samplestatus.reporter.services

import ch.ethz.sis.openbis.generic.asapi.v3.IApplicationServerApi
import ch.systemsx.cisd.common.spring.HttpInvokerUtils
import life.qbic.samplestatus.reporter.Sample
import life.qbic.samplestatus.reporter.api.LimsQueryService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
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
@Component
@ConfigurationProperties
class RealLimsQueryService implements LimsQueryService {

    private IApplicationServerApi openBisApplicationServerApi

    private final String sessionToken

    RealLimsQueryService(@Value('${service.openbis.user}') String openbisUser,
                         @Value('${service.openbis.password}') String openbisPassword,
                         @Value('${service.openbis.api-server-url}') String applicationServerUrl) {
        this.openBisApplicationServerApi = HttpInvokerUtils.createServiceStub(
                IApplicationServerApi.class,
                applicationServerUrl + IApplicationServerApi.SERVICE_URL, 10000)
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
    List<Sample> getUpdatedSamples(Instant updatedSince) {
        // TODO implement sample search @afriedrich
        return null
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
