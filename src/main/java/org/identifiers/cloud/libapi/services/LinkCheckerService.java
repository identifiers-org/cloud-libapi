package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.identifiers.cloud.libapi.models.ServiceRequest;
import org.identifiers.cloud.libapi.models.linkchecker.requests.ScoringRequestWithIdPayload;
import org.identifiers.cloud.libapi.models.linkchecker.requests.ServiceRequestScoreProvider;
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
 *
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
     * @param request on which to perform the preparation steps
     */
    private void prepareScoringRequest(ServiceRequest request) {
        request.setApiVersion(apiVersion);
    }

    public ResponseEntity<ServiceResponseScoringRequest> getScoreForProvider(String providerId, String url) {
        ServiceResponseScoringRequest response = createDefaultResponse();
        String endpoint = String.format("%s/getScoreForProvider");
        ServiceRequestScoreProvider requestBody = new ServiceRequestScoreProvider();
        prepareScoringRequest(requestBody);
        requestBody.setPayload(new ScoringRequestWithIdPayload().setId(providerId));
        requestBody.getPayload().setUrl(url);
        // Prepare the request entity
        RequestEntity<ServiceRequestScoreProvider> requestEntity = null;
        try {
            requestEntity = RequestEntity.post(new URI(endpoint)).body(requestBody);
        } catch (URISyntaxException e) {
            logger.error("INVALID URI '{}'", endpoint);
        }
        // Make the request
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(Configuration.responseErrorHandler());
        return restTemplate.exchange(requestEntity, ServiceResponseScoringRequest.class);
    }

    public ResponseEntity<ServiceResponseScoringRequest> getScoreForResolvedId(String resourceId, String url) {
        // TODO
    }

    // TODO
}
