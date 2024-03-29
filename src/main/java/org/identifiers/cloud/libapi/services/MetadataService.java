package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.identifiers.cloud.libapi.models.metadata.*;
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
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-07 13:42
 * ---
 *
 * This class implements a cient to the Metadata service API.
 */
public class MetadataService {
    public static final String apiVersion = "1.0";
    private static final Logger logger = LoggerFactory.getLogger(MetadataService.class);
    // Re-try pattern, externalize this later if needed
    private RetryTemplate retryTemplate = Configuration.retryTemplate();
    private String serviceApiBaseline;

    private MetadataService() {

    }

    MetadataService(String host, String port) {
        serviceApiBaseline = String.format("http://%s:%s", host, port);
    }

    /**
     * This helper method prepares a default or baseline response to a metadata fetch request for a given Compact ID,
     * customized with the given HTTP Status and error message.
     *
     * A default response when requesting metadata for a given Compact ID is a response that contains no metadata.
     * @param httpStatus custom HTTP Status to set in the default response being created
     * @param errorMessage custom error message to set in the default response being created
     * @return a default response for a metadata fetching request on a Compact ID
     */
    private ServiceResponseFetchMetadata createDefaultResponseFetchMetadata(HttpStatus httpStatus, String errorMessage) {
        ServiceResponseFetchMetadata response = new ServiceResponseFetchMetadata();
        response.setApiVersion(apiVersion)
                .setHttpStatus(httpStatus)
                .setErrorMessage(errorMessage);
        response.setPayload(new ResponseFetchMetadataPayload().setMetadata(""));
        return response;
    }

    /**
     * This helper method prepares a default or baseline response to a metadata fetch request for a given URL, customized with
     * the given HTTP Status and error message.
     *
     * A default response when requesting metadata for a given URL is a response that contains no metadata.
     * @param httpStatus custom HTTP Status to set in the default response being created
     * @param errorMessage custom error message to set in the default response being created
     * @return a default response for a metadata fetching request on a given URL
     */
    private ServiceResponseFetchMetadataForUrl createDefaultResponseFetchMetadataForUrl(HttpStatus httpStatus, String errorMessage) {
        ServiceResponseFetchMetadataForUrl response = new ServiceResponseFetchMetadataForUrl();
        response.setApiVersion(apiVersion)
                .setHttpStatus(httpStatus)
                .setErrorMessage(errorMessage);
        ResponseFetchMetadataForUrlPayload payload = new ResponseFetchMetadataForUrlPayload();
        payload.setMetadata("");
        response.setPayload(payload);
        return response;
    }

    /**
     * This helper method sends an HTTP GET request to the given service endpoint, expecting a metadata fetch request response
     * back from it.
     * @param serviceApiEndpoint this is the endpoint URL where to submit the request to.
     * @return a metadata fetch request response.
     */
    private ServiceResponseFetchMetadata doRequestFetchMetadata(String serviceApiEndpoint) {
        ServiceResponseFetchMetadata response = createDefaultResponseFetchMetadata(HttpStatus.OK, "");
        try {
            ResponseEntity<ServiceResponseFetchMetadata> requestResponse = retryTemplate.execute(retryContext -> {
                // Make the request
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(Configuration.responseErrorHandler());
                return restTemplate.getForEntity(serviceApiEndpoint, ServiceResponseFetchMetadata.class);
            });
            response = requestResponse.getBody();
            response.setHttpStatus(HttpStatus.valueOf(requestResponse.getStatusCode().value()));
            if (HttpStatus.valueOf(requestResponse.getStatusCode().value()) != HttpStatus.OK) {
                String errorMessage = String.format("ERROR fetching metadata " +
                                "at '%s', " +
                                "HTTP status code '%d', " +
                                "explanation '%s'",
                        serviceApiEndpoint,
                        requestResponse.getStatusCode().value(),
                        requestResponse.getBody().getErrorMessage());
                logger.error(errorMessage);
            }
        } catch (RuntimeException e) {
            // Make sure we return a default response in case anything bad happens
            String errorMessage = String.format("ERROR resolving Compact ID at '%s' " +
                    "because of '%s'", serviceApiEndpoint, e.getMessage());
            logger.error(errorMessage);
            response = createDefaultResponseFetchMetadata(HttpStatus.BAD_REQUEST, errorMessage);
        }
        return response;
    }

    /**
     * This helper method prepares the HTTP POST request for fetching metadata from a given URL.
     * @param url URL to fetch metadata from.
     * @param serviceApiEndpoint service endpoint URL for this request.
     * @return a request entity with all set to go, including the request body.
     */
    private RequestEntity<ServiceRequestFetchMetadataForUrl> prepareRequestFetchMetadataForUrl(String url,
                                                                                               String serviceApiEndpoint) {
        // Prepare the request body
        ServiceRequestFetchMetadataForUrl requestBody = new ServiceRequestFetchMetadataForUrl();
        requestBody.setApiVersion(apiVersion);
        requestBody.setPayload(new RequestFetchMetadataForUrlPayload().setUrl(url));
        // Prepare the request entity
        RequestEntity<ServiceRequestFetchMetadataForUrl> request = null;
        try {
            request = RequestEntity.post(new URI(serviceApiEndpoint)).body(requestBody);
        } catch (URISyntaxException e) {
            logger.error("INVALID URI '{}'", serviceApiEndpoint);
        }
        return request;
    }

    /**
     * This is a helper method that will actually submit the HTTP POST request of metadata fetch for URL, already
     * prepared, and it will expect the right response from the service.
     * @param request already prepared metadata fetch request for URL
     * @return response from the service
     */
    private ResponseEntity<ServiceResponseFetchMetadataForUrl> makeRequestFetchMetadataForUrl(RequestEntity<ServiceRequestFetchMetadataForUrl> request) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(Configuration.responseErrorHandler());
        return restTemplate.exchange(request, ServiceResponseFetchMetadataForUrl.class);
    }

    // --- API ---

    /**
     * Metadata API Service: get metadata information for a given Compact ID.
     *
     * Whatever happens with the request, at least a default (empty metadata) response is guaranteed, with the HTTP
     * Status code and error message fields set accordingly.
     * @param compactId Compact ID for which to fetch metadata information.
     * @return service response, that includes potentially found metadata, or indications on what could have happened
     * via HTTP Status codes and additional information on the error message field.
     */
    public ServiceResponseFetchMetadata getMetadataForCompactId(String compactId) {
        String serviceApiEndpoint = String.format("%s/%s", serviceApiBaseline, compactId);
        return doRequestFetchMetadata(serviceApiEndpoint);
    }

    /**
     * Metadata API Service: get metadata information for a given Compact ID, from the provider specified by the given
     * 'selector' / 'provider code'
     *
     * Whatever happens with the request, at least a default (empty metadata) response is guaranteed, with the HTTP
     * Status code and error message fields set accordingly.
     * @param compactId Compact ID for which to fetch metadata information.
     * @param selector provider code of the resource where the metadata should be extracted from
     * @return service response, that includes potentially found metadata, or indications on what could have happened
     * via HTTP Status codes and additional information on the error message field.
     */
    public ServiceResponseFetchMetadata getMetadataForCompactId(String compactId, String selector) {
        String serviceApiEndpoint = String.format("%s/%s/%s", serviceApiBaseline, selector, compactId);
        return doRequestFetchMetadata(serviceApiEndpoint);
    }

    /**
     * Metadata API Service: get metadata information for the given raw request.
     * This method is very useful for those cases where we don't want to use the built-in models for building the
     * request path from a given compact identifier with its possible selector.
     * @param rawRequest raw request
     * @return service response, that includes potentially found metadata, or indications on what could have happened
     * via HTTP Status codes and additional information on the error message field.
     */
    public ServiceResponseFetchMetadata getMetadataForRawRequest(String rawRequest) {
        String serviceApiEndpoint = String.format("%s/%s", serviceApiBaseline, rawRequest);
        return doRequestFetchMetadata(serviceApiEndpoint);
    }

    /**
     * Metadata Service API: get metadata information for a given URL.
     *
     * Whatever happens with the request, at least a default (empty metadata) response is guaranteed, with the HTTP
     * Status code and error message fields set accordingly.
     * @param url URL from which metadata wants to be extracted.
     * @return service response, that includes potentially found metadata, or indications on what could have happened
     * via HTTP Status codes and additional information on the error message field.
     */
    public ServiceResponseFetchMetadataForUrl getMetadataForUrl(String url) {
        String serviceApiEndpoint = String.format("%s/getMetadataForUrl", serviceApiBaseline);
        ServiceResponseFetchMetadataForUrl response = createDefaultResponseFetchMetadataForUrl(HttpStatus.OK, "");
        logger.info("Requesting metadata for URL '{}' at service '{}'", url, serviceApiEndpoint);
        // Prepare the request
        RequestEntity<ServiceRequestFetchMetadataForUrl> request = prepareRequestFetchMetadataForUrl(url, serviceApiEndpoint);
        try {
            ResponseEntity<ServiceResponseFetchMetadataForUrl> requestResponse = retryTemplate.execute(retryContext -> {
                // Do the actual request
                if (request != null) {
                    return makeRequestFetchMetadataForUrl(request);
                }
                // If we get here, send back a custom-made error response
                return new ResponseEntity<>(createDefaultResponseFetchMetadataForUrl(HttpStatus.BAD_REQUEST,
                        String.format("INVALID URI %s", serviceApiEndpoint)),
                        HttpStatus.BAD_REQUEST);
            });
            // Set the response to return to the client
            response = requestResponse.getBody();
            // Set actual HTTP Status in the response body
            response.setHttpStatus(HttpStatus.valueOf(requestResponse.getStatusCode().value()));
            if (HttpStatus.valueOf(requestResponse.getStatusCode().value()) != HttpStatus.OK) {
                String errorMessage = String.format("ERROR retrieving metadata for URL '%s' " +
                                "at '%s', " +
                                "HTTP status code '%d', " +
                                "explanation '%s'",
                        url,
                        serviceApiEndpoint,
                        requestResponse.getStatusCode().value(),
                        requestResponse.getBody().getErrorMessage());
                logger.error(errorMessage);
            }
        } catch (RuntimeException e) {
            // Make sure we return a default response in case anything bad happens
            String errorMessage = String.format("ERROR retrieving resource recommendations from '%s' " +
                    "because of '%s'", serviceApiEndpoint, e.getMessage());
            logger.error(errorMessage);
            response = createDefaultResponseFetchMetadataForUrl(HttpStatus.BAD_REQUEST, errorMessage);
        }
        return response;
    }
}
