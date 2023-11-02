package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.identifiers.cloud.libapi.models.resourcerecommender.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-06 11:35
 * ---
 *
 * This class implements a client to the Resource Recommender service API.
 */
public class ResourceRecommenderService {
    public static final String apiVersion = "1.0";
    private static final Logger logger = LoggerFactory.getLogger(ResourceRecommenderService.class);
    // Re-try pattern, externalize this later if needed
    private RetryTemplate retryTemplate = Configuration.retryTemplate();
    private String serviceApiBaseline;

    private ResourceRecommenderService() {
        // TODO - I don't think I need this default constructor
    }

    ResourceRecommenderService(String serviceHost, String servicePort) {
        serviceApiBaseline = String.format("http://%s:%s", serviceHost, servicePort);
    }

    // TODO - Extend this in the future to support HTTPS

    /**
     * This helper method prepares the recommendation request, for a given list of resolved resources and a given
     * service endpoint.
     * @param resources list of resolved resources for which we want to get a recommendation index.
     * @param serviceApiEndpoint the service end point where the request should be submitted to.
     * @return a RequestEntity with all the given information.
     */
    private RequestEntity<ServiceRequestRecommend> prepareRecommendRequest(List<ResolvedResource> resources,
                                                                           String serviceApiEndpoint) {
        // Prepare the request body
        ServiceRequestRecommend requestBody = new ServiceRequestRecommend();
        requestBody.setApiVersion(apiVersion);
        requestBody.setPayload(new RequestRecommendPayload().setResolvedResources(resources));
        // Prepare the request entity
        RequestEntity<ServiceRequestRecommend> request = null;
        try {
            request = RequestEntity.post(new URI(serviceApiEndpoint)).body(requestBody);
        } catch (URISyntaxException e) {
            logger.error("INVALID URI '{}'", serviceApiEndpoint);
        }
        return request;
    }

    /**
     * Helper method to submit the request and collect a ResponseEntity from the service response.
     * @param request the request to be submitted.
     * @return a ResponseEntity out of the received service response.
     */
    private ResponseEntity<ServiceResponseRecommend> makeRequest(RequestEntity<ServiceRequestRecommend> request) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(Configuration.responseErrorHandler());
        return restTemplate.exchange(request, ServiceResponseRecommend.class);
    }

    /**
     * This helper method creates a default recommendation request response, with contains empty recommendations,
     * customized with the given HTTP Status code and error message information.
     * @param httpStatus HTTP Status to set in the service response being created.
     * @param errorMessage error message to set in the service resopnse being created.
     * @return a recommend request service response, with empty recommendation data, and customized HTTP Status code and
     * error information.
     */
    private ServiceResponseRecommend createDefaultResponse(HttpStatus httpStatus, String errorMessage) {
        ServiceResponseRecommend response = new ServiceResponseRecommend();
        response
                .setApiVersion(apiVersion)
                .setHttpStatus(httpStatus)
                .setErrorMessage(errorMessage);
        response.setPayload(new ResponseRecommendPayload().setResourceRecommendations(new ArrayList<>()));
        return response;
    }

    // --- API ---
    /**
     * Resource Recommender Service API: get recommendation indexes / scores for a given list of resolved resources.
     * @param resources the list of resolved resources to get recommendation data on
     * @return a recommend request response from the service or, a guaranteed default response customized with the HTTP
     * Status code and the error message that describe what could have happened.
     */
    public ServiceResponseRecommend requestRecommendations(final List<ResolvedResource> resources) {
        String serviceApiEndpoint = serviceApiBaseline;
        ServiceResponseRecommend response = createDefaultResponse(HttpStatus.OK, "");
        logger.info("Looking for resource recommendations at '{}'", serviceApiEndpoint);
        if (!resources.isEmpty()) {
            // Prepare the request
            RequestEntity<ServiceRequestRecommend> request = prepareRecommendRequest(resources, serviceApiEndpoint);
            try {
                ResponseEntity<ServiceResponseRecommend> requestResponse = retryTemplate.execute(retryContext -> {
                    // Do the actual request
                    if (request != null) {
                        return makeRequest(request);
                    }
                    // If we get here, send back a custom-made error response
                    return new ResponseEntity<>(createDefaultResponse(HttpStatus.BAD_REQUEST,
                            String.format("INVALID URI %s", serviceApiEndpoint)),
                            HttpStatus.BAD_REQUEST);
                });
                // Set the response to return to the client
                response = requestResponse.getBody();
                // Set actual HTTP Status in the response body
                response.setHttpStatus(HttpStatus.valueOf(requestResponse.getStatusCode().value()));
                if (HttpStatus.valueOf(requestResponse.getStatusCode().value()) != HttpStatus.OK) {
                    String errorMessage = String.format("ERROR retrieving resource recommendations " +
                                    "from '%s', " +
                                    "HTTP status code '%d', " +
                                    "explanation '%s'",
                            serviceApiEndpoint,
                            requestResponse.getStatusCode().value(),
                            requestResponse.getBody().getErrorMessage());
                    // TODO - Maybe I should set this message within the response as well
                    logger.error(errorMessage);
                }
            } catch (RuntimeException e) {
                // Make sure we return a default response in case anything bad happens
                String errorMessage = String.format("ERROR retrieving resource recommendations from '%s' " +
                        "because of '%s'", serviceApiEndpoint, e.getMessage());
                logger.error(errorMessage);
                response = createDefaultResponse(HttpStatus.BAD_REQUEST, errorMessage);
            }
        }
        return response;
    }
}