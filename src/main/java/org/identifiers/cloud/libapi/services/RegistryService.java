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
 *
 * This class implements a client to the Registry service API.
 */
public class RegistryService {
    private static final String apiVersion = "1.0";
    private static Logger logger = LoggerFactory.getLogger(RegistryService.class);
    // Re-try pattern, externalize this later if needed
    private RetryTemplate retryTemplate = Configuration.retryTemplate();
    private String serviceApiBaseline;

    private RegistryService() {

    }

    RegistryService(String host, String port) {
        serviceApiBaseline = String.format("http://%s:%s", host, port);
    }

    /**
     * This generic helper will initialize a default response from the registry service, with given payload, and api
     * version information as well as a default HTTP Status code.
     * @param response service response to initialize.
     * @param payload payload to use for response initialization.
     * @param <T> this parameter is the type of the payload that the given response is specialized on.
     */
    private <T> void initDefaultResponse(ServiceResponse<T> response, T payload) {
        response.setApiVersion(apiVersion)
                .setHttpStatus(HttpStatus.OK);
        response.setPayload(payload);
    }

    /**
     * This helper creates a default response for the validation request interface of the registry service.
     * @return default validation request response
     */
    private ServiceResponseValidateRequest createDefaultResponseValidationRequest() {
        ServiceResponseValidateRequest response = new ServiceResponseValidateRequest();
        initDefaultResponse(response, new ServiceResponseRegisterPrefixPayload());
        return response;
    }

    /**
     * This helper creates a default response for the prefix registration request interface of the registry service.
     * @return default prefix registration response.
     */
    private ServiceResponseRegisterPrefix createDefaultResponseRegisterPrefixRequest() {
        ServiceResponseRegisterPrefix response = new ServiceResponseRegisterPrefix();
        initDefaultResponse(response, new ServiceResponseRegisterPrefixPayload());
        return response;
    }

    /**
     * This helper method initializes the given request with common request data, e.g. api version information.
     * @param request request to initialize.
     */
    private void initRequest(ServiceRequest<?> request) {
        request.setApiVersion(apiVersion);
    }

    /**
     * This helper method creates a request for the prefix registration interface of the registry service.
     * @param payload body of the request.
     * @return the created service request.
     */
    private ServiceRequestRegisterPrefix createRequestRegisterPrefix(ServiceRequestRegisterPrefixPayload payload) {
        ServiceRequestRegisterPrefix request = new ServiceRequestRegisterPrefix();
        initRequest(request);
        request.setPayload(payload);
        return request;
    }

    /**
     * This helper method creates a request for the validation interface of the registry service.
     * @param payload body of the request.
     * @return the created service request.
     */
    private ServiceRequestValidate createRequestValidationRequest(ServiceRequestRegisterPrefixPayload payload) {
        ServiceRequestValidate request = new ServiceRequestValidate();
        initRequest(request);
        request.setPayload(payload);
        return request;
    }

    /**
     * This is a generic helper method that prepares an Entity Request, used by the different kinds of POST requests
     * this service client implements.
     * @param requestBody body for the POST request
     * @param serviceApiEndpoint service enpoint for the request.
     * @param <T> type of the request body
     * @return the entity request object, or null if there was a problem creating it, e.g. the provided service endpoint
     * was invalid
     */
    private <T> RequestEntity<T> prepareEntityRequest(T requestBody, String serviceApiEndpoint) {
        RequestEntity<T> entityRequest = null;
        try {
            entityRequest = RequestEntity.post(new URI(serviceApiEndpoint)).body(requestBody);
        } catch (URISyntaxException e) {
            logger.error("INVALID URI '{}'", serviceApiEndpoint);
        }
        return entityRequest;
    }

    /**
     * Helper method to instantiate rest templates used for submitting requests to the registry service.
     *
     * It will also set the right error handler in the rest template instance.
     *
     * @return a new instance of the RestTemplate
     */
    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(Configuration.responseErrorHandler());
        return restTemplate;
    }

    /**
     * Helper method that submits a given prefix registration requests to the registry service and expects a response
     * accordingly.
     * @param request prefix registration request to be submitted.
     * @return a ResponseEntity specialized with the expected prefix registration request response type.
     */
    private ResponseEntity<ServiceResponseRegisterPrefix> doRegisterPrefixRequest(RequestEntity<ServiceRequestRegisterPrefix> request) {
        return getRestTemplate().exchange(request, ServiceResponseRegisterPrefix.class);
    }

    /**
     * Helper method that submits a given validation requests to the registry service and expects a response accordingly.
     * @param request validation request to be submitted.
     * @return a ResponseEntity specialized with the expected validation request response type.
     */
    private ResponseEntity<ServiceResponseValidateRequest> doValidateRequest(RequestEntity<ServiceRequestValidate> request) {
        return getRestTemplate().exchange(request, ServiceResponseValidateRequest.class);
    }

    /**
     * This helper method will submit a validation request to the given service endpoint, using the given payload.
     * Expecting the validation request response from the registry service.
     * @param serviceApiEndpoint service endpoint where to submit the request.
     * @param payload data content for this request.
     * @return validation request response from the registry service, or a guaranteed default response reflecting what
     * could have happened via its HTTP Status code and error message fields.
     */
    private ServiceResponseValidateRequest requestValidation(String serviceApiEndpoint,
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
    /**
     * Registry Service API: request prefix registration for the given prefix registration information.
     * @param registrationPayload prefix registration information.
     * @return prefix registration request response or a guaranteed default response, for this context, where HTTP
     * Status code and error message fields have information on what could have happened.
     */
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

    /**
     * Registry Service API: validate 'name' field for a prefix registration request.
     *
     * This will not validate the whole prefix registration request, only the 'name' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest requestValidationName(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixName"), payload);
    }

    /**
     * Registry Service API: validate 'description' field for a prefix registration request.
     *
     * This will not validate the whole prefix registration request, only the 'description' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest requestValidationDescription(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixDescription"), payload);
    }

    /**
     * Registry Service API: validate 'home page' field for a prefix registration request.
     *
     * This will not validate the whole prefix registration request, only the 'home page' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest requestValidationHomePage(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixHomePage"), payload);
    }

    /**
     * Registry Service API: validate 'organization' field for a prefix registration request.
     *
     * This will not validate the whole prefix registration request, only the 'organization' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest requestValidationOrganization(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixOrganization"), payload);
    }

    /**
     * Registry Service API: validate 'preferred prefix' field for a prefix registration request.
     *
     * This will not validate the whole prefix registration request, only the 'preferred prefix' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest requestValidationPreferredPrefix(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixPreferredPrefix"), payload);
    }

    public ServiceResponseValidateRequest requestValidationResourceAccessRule(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixResourceAccessRule"), payload);
    }

    public ServiceResponseValidateRequest requestValidationExampleIdentifier(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixExampleIdentifier"), payload);
    }

    public ServiceResponseValidateRequest requestValidationRegexPattern(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixRegexPattern"), payload);
    }

    public ServiceResponseValidateRequest requestValidationReferences(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixReferences"), payload);
    }

    public ServiceResponseValidateRequest requestValidationAdditionalInformation(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixAdditionalInformation"), payload);
    }

    public ServiceResponseValidateRequest requestValidationRequester(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", serviceApiBaseline, "validateRegisterPrefixRequester"), payload);
    }

}
