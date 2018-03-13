package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.identifiers.cloud.libapi.models.resolver.ResponseResolvePayload;
import org.identifiers.cloud.libapi.models.resolver.ServiceResponseResolve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-07 10:04
 * ---
 *
 * This class implements a client to the Resolver service API.
 */
public class ResolverService {
    public static final String apiVersion = "1.0";
    private static final Logger logger = LoggerFactory.getLogger(ResolverService.class);
    // Re-try pattern, externalize this later if needed
    private RetryTemplate retryTemplate = Configuration.retryTemplate();
    private String serviceApiBaseline;

    private ResolverService() {

    }

    ResolverService(String host, String port) {
        serviceApiBaseline = String.format("http://%s:%s", host, port);
    }

    /**
     * This helper method creates a default / baseline response for a resolution request, where the resolved resources
     * are empty, the api version information is properly set, and the HTTP Status code and error message fields are
     * customized to the given parameters.
     * @param httpStatus HTTP Status to set in the default response object.
     * @param errorMessage error message to set in the default response object.
     * @return a customized default resolution request service response.
     */
    private ServiceResponseResolve createDefaultResponse(HttpStatus httpStatus, String errorMessage) {
        ServiceResponseResolve response = new ServiceResponseResolve();
        response
                .setApiVersion(apiVersion)
                .setHttpStatus(httpStatus)
                .setErrorMessage(errorMessage);
        response.setPayload(new ResponseResolvePayload().setResolvedResources(new ArrayList<>()));
        return response;
    }

    /**
     * Helper method that submits a resolution HTTP GET request to the resolver service, and expects the corresponding
     * response.
     * @param serviceApiEndpoint service endpoint for the HTTP GET request.
     * @return resolution request response from the service or a guaranteed default response, where HTTP Status code and
     * error message contains information on what could have happened with the request.
     */
    private ServiceResponseResolve doRequestResolution(String serviceApiEndpoint) {
        ServiceResponseResolve response = createDefaultResponse(HttpStatus.OK, "");
        try {
            ResponseEntity<ServiceResponseResolve> requestResponse = retryTemplate.execute(retryContext -> {
                // Make the request
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(Configuration.responseErrorHandler());
                return restTemplate.getForEntity(serviceApiEndpoint, ServiceResponseResolve.class);
            });
            response = requestResponse.getBody();
            response.setHttpStatus(HttpStatus.valueOf(requestResponse.getStatusCodeValue()));
            if (HttpStatus.valueOf(requestResponse.getStatusCodeValue()) != HttpStatus.OK) {
                String errorMessage = String.format("ERROR resolving Compact ID " +
                                "at '%s', " +
                                "HTTP status code '%d', " +
                                "explanation '%s'",
                        serviceApiEndpoint,
                        requestResponse.getStatusCodeValue(),
                        requestResponse.getBody().getErrorMessage());
                logger.error(errorMessage);
            }
        } catch (RuntimeException e) {
            // Make sure we return a default response in case anything bad happens
            String errorMessage = String.format("ERROR resolving Compact ID at '%s' " +
                    "because of '%s'", serviceApiEndpoint, e.getMessage());
            logger.error(errorMessage);
            response = createDefaultResponse(HttpStatus.BAD_REQUEST, errorMessage);
        }
        return response;
    }

    // --- API ---

    /**
     * Resolver Service API: resolve a given Compact ID to a list of possible resources / providers of information on
     * that Compact ID.
     * @param compactId Compact ID to resolve.
     * @return the service response, containing the resolution information, or a default guaranteed response (with an
     * empty list of resources / providers) where the HTTP Status code and error message fields contain infromation on
     * what could have happened to the request.
     */
    public ServiceResponseResolve requestCompactIdResolution(String compactId) {
        String serviceApiEndpoint = String.format("%s/%s", serviceApiBaseline, compactId);
        return doRequestResolution(serviceApiEndpoint);
    }

    public ServiceResponseResolve requestCompactIdResolution(String compactId, String selector) {
        String serviceApiEndpoint = String.format("%s/%s/%s", serviceApiBaseline, selector, compactId);
        return doRequestResolution(serviceApiEndpoint);
    }
}
