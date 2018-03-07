package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.identifiers.cloud.libapi.models.metadata.ResponseFetchMetadataForUrlPayload;
import org.identifiers.cloud.libapi.models.metadata.ResponseFetchMetadataPayload;
import org.identifiers.cloud.libapi.models.metadata.ServiceResponseFetchMetadata;
import org.identifiers.cloud.libapi.models.metadata.ServiceResponseFetchMetadataForUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-07 13:42
 * ---
 */
public class MetadataService {
    public static final String apiVersion = "1.0";
    private static final Logger logger = LoggerFactory.getLogger(MetadataService.class);
    // Re-try pattern, externalize this later if needed
    private RetryTemplate retryTemplate = Configuration.retryTemplate();
    private String serviceApiBaseline;

    public MetadataService(String host, String port) {
        serviceApiBaseline = String.format("http://%s:%s", host, port);
    }

    private ServiceResponseFetchMetadata createDefaultResponseFetchMetadata(HttpStatus httpStatus, String errorMessage) {
        ServiceResponseFetchMetadata response = new ServiceResponseFetchMetadata();
        response.setApiVersion(apiVersion)
                .setHttpStatus(httpStatus)
                .setErrorMessage(errorMessage);
        response.setPayload(new ResponseFetchMetadataPayload().setMetadata(""));
        return response;
    }

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
            response.setHttpStatus(HttpStatus.valueOf(requestResponse.getStatusCodeValue()));
            if (HttpStatus.valueOf(requestResponse.getStatusCodeValue()) != HttpStatus.OK) {
                String errorMessage = String.format("ERROR fetching metadata " +
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
            response = createDefaultResponseFetchMetadata(HttpStatus.BAD_REQUEST, errorMessage);
        }
        return response;
    }

    // --- API ---
    public ServiceResponseFetchMetadata getMetadataForCompactId(String compactId) {
        String serviceApiEndpoint = String.format("%s/%s", serviceApiBaseline, compactId);
        return doRequestFetchMetadata(serviceApiEndpoint);
    }
}
