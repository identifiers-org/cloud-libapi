package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.identifiers.cloud.libapi.models.ServiceRequest;
import org.identifiers.cloud.libapi.models.ServiceResponse;
import org.identifiers.cloud.libapi.models.registry.requests.prefixregistration.ServiceRequestRegisterPrefix;
import org.identifiers.cloud.libapi.models.registry.requests.validation.ServiceRequestValidate;
import org.identifiers.cloud.libapi.models.registry.responses.prefixregistration.ServiceResponseRegisterPrefix;
import org.identifiers.cloud.libapi.models.registry.responses.prefixregistration.ServiceResponseRegisterPrefixPayload;
import org.identifiers.cloud.libapi.models.registry.responses.validation.ServiceResponseValidateRequest;
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
 * Timestamp: 2018-03-08 11:31
 * ---
 */
public class RegistryService {
    private static final String apiVersion = "1.0";
    private static Logger logger = LoggerFactory.getLogger(RegistryService.class);
    // Re-try pattern, externalize this later if needed
    private RetryTemplate retryTemplate = Configuration.retryTemplate();
    private String serviceApiBaseline;

    public RegistryService(String host, String port) {
        serviceApiBaseline = String.format("http://%s:%s", host, port);
    }

    private <T> void initDefaultResponse(ServiceResponse<T> response, T payload) {
        response.setApiVersion(apiVersion)
                .setHttpStatus(HttpStatus.OK);
        response.setPayload(payload);
    }

    private ServiceResponseValidateRequest createDefaultResponseValidationRequest() {
        ServiceResponseValidateRequest response = new ServiceResponseValidateRequest();
        initDefaultResponse(response, new ServiceResponseRegisterPrefixPayload());
        return response;
    }

    private ServiceResponseRegisterPrefix createDefaultResponseRegisterPrefixRequest() {
        ServiceResponseRegisterPrefix response = new ServiceResponseRegisterPrefix();
        initDefaultResponse(response, new ServiceResponseRegisterPrefixPayload());
        return response;
    }

    private void initRequest(ServiceRequest<?> request) {
        request.setApiVersion(apiVersion);
    }

    private ServiceRequestRegisterPrefix createRequestRegisterPrefix() {
        ServiceRequestRegisterPrefix request = new ServiceRequestRegisterPrefix();
        initRequest(request);
        return request;
    }

    private ServiceRequestValidate createRequestValidationRequest() {
        ServiceRequestValidate request = new ServiceRequestValidate();
        initRequest(request);
        return request;
    }

    private <T> RequestEntity<T> prepareEntityRequest(T requestBody, String serviceApiEndpoint) {
        RequestEntity<T> entityRequest = null;
        try {
            entityRequest = RequestEntity.post(new URI(serviceApiEndpoint)).body(requestBody);
        } catch (URISyntaxException e) {
            logger.error("INVALID URI '{}'", serviceApiEndpoint);
        }
        return entityRequest;
    }

    private <T, E> ResponseEntity<T> doRequest(RequestEntity<E> request, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(Configuration.responseErrorHandler());
        return restTemplate.exchange(request, responseType);
    }
}
