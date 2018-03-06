package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.models.ResourceRecommender.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
 */
public class ResourceRecommenderService {
    public static final String apiVersion = "1.0";
    public static final int WS_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    public static final int WS_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds

    private static final Logger logger = LoggerFactory.getLogger(ResourceRecommenderService.class);

    // Re-try pattern, externalize this later if needed
    private static final RetryTemplate retryTemplate;

    static {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(WS_REQUEST_RETRY_MAX_ATTEMPTS);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(WS_REQUEST_RETRY_BACK_OFF_PERIOD);

        retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
    }

    private String resourceRecommenderServiceHost;
    private String resourceRecommenderServicePort;

    // Error handler for the request
    class RestTemplateErrorHandler implements ResponseErrorHandler {
        ClientHttpResponse clientHttpResponse;

        @Override
        public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
            // We're going to say that it has no error so we can't handle this properly
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
            logger.error("The following error came back from the Recommender Service, HTTP Status #{}, error content '{}'",
                    clientHttpResponse.getRawStatusCode(),
                    clientHttpResponse.getStatusText());
        }
    }

    public ServiceResponseRecommend getRecommendations(final List<ResolvedResource> resources) {
        String serviceApiEndpoint = String.format("http://%s:%s", resourceRecommenderServiceHost,
                resourceRecommenderServicePort);
        ServiceResponseRecommend response = new ServiceResponseRecommend();
        response.setHttpStatus(HttpStatus.I_AM_A_TEAPOT);
        logger.info("Looking for resource recommendations at '{}'", serviceApiEndpoint);
        if (!resources.isEmpty()) {
            try {
                ResponseEntity<ServiceResponseRecommend> requestResponse = retryTemplate.execute(retryContext -> {
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
                    // Do the actual request
                    RestTemplate restTemplate = new RestTemplate();
                    if (request != null)
                        return restTemplate.exchange(request, ServiceResponseRecommend.class);
                    // If we get here, send back a custom made error response
                    ServiceResponseRecommend errorResponse = new ServiceResponseRecommend();
                    errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
                    errorResponse.setErrorMessage(String.format("INVALID URI %s", serviceApiEndpoint));
                    errorResponse.setPayload(new ResponseRecommendPayload().setResourceRecommendations(new ArrayList<>()));
                    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
                });
                // Set the response to return to the client
                response = requestResponse.getBody();
                // Set actual HTTP Status in the response body
                response.setHttpStatus(HttpStatus.valueOf(requestResponse.getStatusCodeValue()));
                if (HttpStatus.valueOf(requestResponse.getStatusCodeValue()) == HttpStatus.OK) {
                    // TODO We got a valid response
                    logger.debug("We got recommendations!");
                }
                else {
                    String errorMessage = String.format("ERROR retrieving resource recommendations " +
                                    "from '%s', " +
                                    "HTTP status code '%d', " +
                                    "explanation '%s'",
                            serviceApiEndpoint,
                            requestResponse.getStatusCodeValue(),
                            requestResponse.getBody().getErrorMessage());
                    logger.error(errorMessage);
                    //throw new ResourceRecommenderStrategyException(errorMessage);
                }
            } catch (RuntimeException e) {
                logger.error("ERROR retrieving resource recommendations from '{}' because of '{}'",
                        serviceApiEndpoint,
                        e.getMessage());
            }
        }
        return response;
    }
}