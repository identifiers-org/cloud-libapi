package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.identifiers.cloud.libapi.models.ServiceRequest;
import org.identifiers.cloud.libapi.models.ServiceResponse;
import org.identifiers.cloud.libapi.models.registry.requests.prefixregistration.ServiceRequestRegisterPrefix;
import org.identifiers.cloud.libapi.models.registry.requests.prefixregistration.ServiceRequestRegisterPrefixPayload;
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

    private ServiceRequestRegisterPrefix createRequestRegisterPrefix(ServiceRequestRegisterPrefixPayload payload) {
        ServiceRequestRegisterPrefix request = new ServiceRequestRegisterPrefix();
        initRequest(request);
        request.setPayload(payload);
        return request;
    }

    private ServiceRequestValidate createRequestValidationRequest(ServiceRequestRegisterPrefixPayload payload) {
        ServiceRequestValidate request = new ServiceRequestValidate();
        initRequest(request);
        request.setPayload(payload);
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

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(Configuration.responseErrorHandler());
        return restTemplate;
    }

    private ResponseEntity<ServiceResponseRegisterPrefix> doRegisterPrefixRequest(RequestEntity<ServiceRequestRegisterPrefix> request) {
        return getRestTemplate().exchange(request, ServiceResponseRegisterPrefix.class);
    }

    private ResponseEntity<ServiceResponseValidateRequest> doValidateRequest(RequestEntity<ServiceRequestValidate> request) {
        return getRestTemplate().exchange(request, ServiceResponseValidateRequest.class);
    }

    public ServiceResponseValidateRequest requestValidation(String serviceApiEndpoint,
                                                            ServiceRequestRegisterPrefixPayload payload) {
        ServiceResponseValidateRequest response = createDefaultResponseValidationRequest();
        logger.info("Requesting validation at '{}'", serviceApiEndpoint);
        RequestEntity<ServiceRequestValidate> requestEntity =
                prepareEntityRequest(createRequestValidationRequest(payload),
                        serviceApiEndpoint);
        try {
            ResponseEntity<ServiceResponseValidateRequest> responseEntity = retryTemplate.execute(retryContext -> {
                if (requestEntity != null) {
                    return doValidateRequest(requestEntity);
                }
                ServiceResponseValidateRequest errorResponse = createDefaultResponseValidationRequest();
                errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST)
                        .setErrorMessage(String.format("INVALID URI %s", serviceApiEndpoint));
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            });
            response = responseEntity.getBody();
            response.setHttpStatus(HttpStatus.valueOf(responseEntity.getStatusCodeValue()));
            if (HttpStatus.valueOf(responseEntity.getStatusCodeValue()) != HttpStatus.OK) {
                String errorMessage = String.format("VALIDATION ERROR " +
                                "at '%s', " +
                                "HTTP status code '%d', " +
                                "explanation '%s'",
                        serviceApiEndpoint,
                        responseEntity.getStatusCodeValue(),
                        responseEntity.getBody().getErrorMessage());
                logger.error(errorMessage);
            }
        } catch (RuntimeException e) {
            String errorMessage = String.format("ERROR while requesting validation at '%s', because of '%s'",
                    serviceApiEndpoint, e.getMessage());
            logger.error(errorMessage);
            response = createDefaultResponseValidationRequest();
            response.setHttpStatus(HttpStatus.BAD_REQUEST).setErrorMessage(errorMessage);
        }
        return response;
    }

    // --- API ---
    public ServiceResponseRegisterPrefix requestPrefixRegistration(ServiceRequestRegisterPrefixPayload registrationPayload) {
        String serviceApiEndpoint = serviceApiBaseline;
        ServiceResponseRegisterPrefix response = createDefaultResponseRegisterPrefixRequest();
        logger.info("Requesting prefix '{}' registration at '{}'", registrationPayload.getPreferredPrefix(),
                serviceApiEndpoint);
        RequestEntity<ServiceRequestRegisterPrefix> requestEntity =
                prepareEntityRequest(createRequestRegisterPrefix(registrationPayload),
                        serviceApiEndpoint);
        try {
            ResponseEntity<ServiceResponseRegisterPrefix> responseEntity = retryTemplate.execute(retryContext -> {
                if (requestEntity != null) {
                    return doRegisterPrefixRequest(requestEntity);
                }
                ServiceResponseRegisterPrefix errorResponse = createDefaultResponseRegisterPrefixRequest();
                errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST)
                        .setErrorMessage(String.format("INVALID URI %s", serviceApiEndpoint));
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            });
            response = responseEntity.getBody();
            response.setHttpStatus(HttpStatus.valueOf(responseEntity.getStatusCodeValue()));
            if (HttpStatus.valueOf(responseEntity.getStatusCodeValue()) != HttpStatus.OK) {
                String errorMessage = String.format("ERROR registering your prefix " +
                                "at '%s', " +
                                "HTTP status code '%d', " +
                                "explanation '%s'",
                        serviceApiEndpoint,
                        responseEntity.getStatusCodeValue(),
                        responseEntity.getBody().getErrorMessage());
                logger.error(errorMessage);
            }
        } catch (RuntimeException e) {
            String errorMessage = String.format("ERROR while registering prefix '%s' at '%s', because of '%s'",
                    registrationPayload.getPreferredPrefix(), serviceApiEndpoint, e.getMessage());
            logger.error(errorMessage);
            response = createDefaultResponseRegisterPrefixRequest();
            response.setHttpStatus(HttpStatus.BAD_REQUEST).setErrorMessage(errorMessage);
        }
        return response;
    }

    public ServiceResponseValidateRequest requestValidationName(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixName"), payload);
    }

    public ServiceResponseValidateRequest requestValidationDescription(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixDescription"), payload);
    }

    public ServiceResponseValidateRequest requestValidationHomePage(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixHomePage"), payload);
    }

    public ServiceResponseValidateRequest requestValidationOrganization(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixOrganization"), payload);
    }


}
