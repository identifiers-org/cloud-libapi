package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.identifiers.cloud.libapi.models.ServiceRequest;
import org.identifiers.cloud.libapi.models.linkchecker.requests.ScoringRequestWithIdPayload;
import org.identifiers.cloud.libapi.models.linkchecker.requests.ServiceRequestScoreProvider;
import org.identifiers.cloud.libapi.models.linkchecker.requests.ServiceRequestScoreResource;
import org.identifiers.cloud.libapi.models.linkchecker.responses.ServiceResponseScoringRequest;
import org.identifiers.cloud.libapi.models.linkchecker.responses.ServiceResponseScoringRequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-06-18 9:47
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This class implements a client for the Link Checker Service.
 */
public class LinkCheckerService {
    public static final String apiVersion = "1.0";
    private static final Logger logger = LoggerFactory.getLogger(LinkCheckerService.class);
    // Re-try pattern
    private RetryTemplate retryTemplate = Configuration.retryTemplate();
    private String serviceApiBaseline;

    LinkCheckerService(String serviceHost, String servicePort) {
        // TODO - This needs to be refactored in the future for supporting multiple schema (HTTP, HTTPS)
        serviceApiBaseline = String.format("http://%s:%s", serviceHost, servicePort);
    }

    /**
     * Helper method that prepares a default scoring response, to guarantee response back from service.
     *
     * @return a default scoring request response
     */
    private ServiceResponseScoringRequest createDefaultResponse() {
        ServiceResponseScoringRequest response = new ServiceResponseScoringRequest();
        response.setApiVersion(apiVersion).setHttpStatus(HttpStatus.OK);
        response.setPayload(new ServiceResponseScoringRequestPayload());
        return response;
    }

    /**
     * This helper will perform any preparation steps needed to get the service request ready for the payload. Right now
     * it only sets the API Version information
     *
     * @param request on which to perform the preparation steps
     */
    private void prepareScoringRequest(ServiceRequest request) {
        request.setApiVersion(apiVersion);
    }

    // --- API ---

    /**
     * Link Checker Service API: Get a Reliability Score for a given Provider ID, i.e. a provider in the context of a
     * namespace / prefix.
     * @param providerId ID of the provider
     * @param url URL of the given provider in the context of a particular namespace / prefix
     * @return a scoring response from the Link Checking Service
     */

    public ServiceResponseScoringRequest getScoreForProvider(String providerId, String url) {
        return getScoreForProvider(providerId, url, false);
    }

    public ServiceResponseScoringRequest getScoreForProvider(String providerId, String url, boolean isProtectedUrls) {
        // NOTE - I still don't like how it looks, but it's a little bit better
        String endpoint = String.format("%s/getScoreForProvider", serviceApiBaseline);
        // Prepare the request body
        ServiceRequestScoreProvider requestBody = new ServiceRequestScoreProvider();
        prepareScoringRequest(requestBody);
        requestBody.setPayload(new ScoringRequestWithIdPayload());
        requestBody.getPayload().setId(providerId);
        requestBody.getPayload().setUrl(url);
        requestBody.getPayload().setAccept401or403(isProtectedUrls);
        // Prepare response
        ServiceResponseScoringRequest response = createDefaultResponse();
        // Prepare the request entity
        RequestEntity<ServiceRequestScoreProvider> requestEntity = null;
        try {
            requestEntity = RequestEntity.post(new URI(endpoint)).body(requestBody);
        } catch (URISyntaxException e) {
            logger.error("INVALID URI '{}'", endpoint);
            response.setHttpStatus(HttpStatus.BAD_REQUEST)
                    .setErrorMessage(String.format("An error occurred while trying score " +
                                    "Provider with ID '%s', URL '%s', using service endpoint '%s' INVALID URI",
                            providerId, url, endpoint));
        }
        if (requestEntity != null) {
            // Make the request using the re-try pattern
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(Configuration.responseErrorHandler());
            try {
                RequestEntity<ServiceRequestScoreProvider> finalRequestEntity = requestEntity;
                ResponseEntity<ServiceResponseScoringRequest> responseEntity =
                        retryTemplate.execute(retryContext ->
                                restTemplate.exchange(finalRequestEntity, ServiceResponseScoringRequest.class));
                response = responseEntity.getBody();
                response.setHttpStatus(responseEntity.getStatusCode());
                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    String errorMessage = String.format("ERROR retrieving reliability scoring information for " +
                                    "Provider ID '%s', URL '%s' " +
                                    "from '%s', " +
                                    "HTTP status code '%d', " +
                                    "explanation '%s'",
                            providerId,
                            url,
                            endpoint,
                            responseEntity.getStatusCodeValue(),
                            responseEntity.getBody().getErrorMessage());
                    logger.error(errorMessage);
                }
            } catch (RuntimeException e) {
                String errorMessage = String.format("ERROR retrieving reliability scoring information for " +
                                "Provider ID '%s', URL '%s' " +
                                "from '%s', " +
                                "explanation '%s'",
                        providerId,
                        url,
                        endpoint,
                        e.getMessage());
                response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR).setErrorMessage(errorMessage);
                logger.error(errorMessage);
            }
        }
        return response;
    }

    ServiceResponseScoringRequest getScoreForResolvedId(String resourceId, String url) {
        return getScoreForResolvedId(resourceId, url, false);
    }

    public ServiceResponseScoringRequest getScoreForResolvedId(String resourceId, String url, boolean isProtectedUrls) {
        String endpoint = String.format("%s/getScoreForResolvedId", serviceApiBaseline);
        // Prepare the request body
        ServiceRequestScoreResource requestBody = new ServiceRequestScoreResource();
        prepareScoringRequest(requestBody);
        requestBody.setPayload(new ScoringRequestWithIdPayload());
        requestBody.getPayload().setId(resourceId);
        requestBody.getPayload().setUrl(url);
        requestBody.getPayload().setAccept401or403(isProtectedUrls);
        // Prepare response
        ServiceResponseScoringRequest response = createDefaultResponse();
        // Prepare the request entity
        RequestEntity<ServiceRequestScoreResource> requestEntity = null;
        try {
            requestEntity = RequestEntity.post(new URI(endpoint)).body(requestBody);
        } catch (URISyntaxException e) {
            logger.error("INVALID URI '{}'", endpoint);
            response.setHttpStatus(HttpStatus.BAD_REQUEST)
                    .setErrorMessage(String.format("An error occurred while trying score " +
                                    "Resource with ID '%s', URL '%s', using service endpoint '%s' INVALID URI",
                            resourceId, url, endpoint));
        }
        if (requestEntity != null) {
            // Make the request using the re-try pattern
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(Configuration.responseErrorHandler());
            try {
                RequestEntity<ServiceRequestScoreResource> finalRequestEntity = requestEntity;
                ResponseEntity<ServiceResponseScoringRequest> responseEntity =
                        retryTemplate.execute(retryContext ->
                                restTemplate.exchange(finalRequestEntity, ServiceResponseScoringRequest.class));
                response = responseEntity.getBody();
                response.setHttpStatus(responseEntity.getStatusCode());
                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    String errorMessage = String.format("ERROR retrieving reliability scoring information for " +
                                    "Resource ID '%s', URL '%s' " +
                                    "from '%s', " +
                                    "HTTP status code '%d', " +
                                    "explanation '%s'",
                            resourceId,
                            url,
                            endpoint,
                            responseEntity.getStatusCodeValue(),
                            responseEntity.getBody().getErrorMessage());
                    logger.error(errorMessage);
                }
            } catch (RuntimeException e) {
                String errorMessage = String.format("ERROR retrieving reliability scoring information for " +
                                "Resource ID '%s', URL '%s' " +
                                "from '%s', " +
                                "explanation '%s'",
                        resourceId,
                        url,
                        endpoint,
                        e.getMessage());
                response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR).setErrorMessage(errorMessage);
                logger.error(errorMessage);
            }
        }
        return response;
    }
}
