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
// TODO Rewriting of this, for the new registry service in HQ
public class RegistryService {
    // API subpath
    public static final String REGISTRY_API_PATH_PREFIX_REGISTRATION = "prefixRegistrationApi";
    private static final String apiVersion = "1.0";
    private static Logger logger = LoggerFactory.getLogger(RegistryService.class);
    // Re-try pattern, externalize this later if needed
    private RetryTemplate retryTemplate = Configuration.retryTemplate();
    // Default protocol scheme is HTTPS
    private String protocolScheme = "https";
    // Default host will be localhost
    private String host;
    // Default port is 80
    private int port = 8180;

    private RegistryService() { }

    RegistryService(String host, String port) {
        this.host = host;
        this.port = Integer.parseInt(port);
    }

    public void setProtocolSchemeToHttp() {
        protocolScheme = "http";
    }

    public void setProtocolSchemeToHttps() {
        protocolScheme = "https";
    }

    private String getServiceApiEndpointBaseline() {
        return String.format("%s://%s:%d/%s", protocolScheme, host, port, REGISTRY_API_PATH_PREFIX_REGISTRATION);
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
        // TODO Should we go for a re-try template here?
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
        String serviceApiEndpoint = getServiceApiEndpointBaseline();
        ServiceResponseRegisterPrefix response = createDefaultResponseRegisterPrefixRequest();
        logger.info("Requesting prefix '{}' registration at '{}'", registrationPayload.getRequestedPrefix(),
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
                    registrationPayload.getRequestedPrefix(), serviceApiEndpoint, e.getMessage());
            logger.error(errorMessage);
            response = createDefaultResponseRegisterPrefixRequest();
            response.setHttpStatus(HttpStatus.BAD_REQUEST).setErrorMessage(errorMessage);
        }
        return response;
    }

    /**
     * Registry Service API: validate the namespace name.
     *
     * This will not validate the whole prefix registration request, only the 'name' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest validateName(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateName"), payload);
    }

    /**
     * Registry Service API: validate the namespace description
     *
     * This will not validate the whole prefix registration request, only the 'description' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest validateDescription(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateDescription"), payload);
    }

    /**
     * Registry Service API: validate the given provider home URL
     *
     * This will not validate the whole prefix registration request, only the 'home page' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest validateProviderHomeUrl(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateProviderHomeUrl"), payload);
    }

    public ServiceResponseValidateRequest validateProviderName(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateProviderName"), payload);
    }

    public ServiceResponseValidateRequest validateProviderDescription(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateProviderDescription"), payload);
    }

    public ServiceResponseValidateRequest validateProviderLocation(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateProviderLocation"), payload);
    }

    public ServiceResponseValidateRequest validateProviderCode(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateProviderCode"), payload);
    }

    /**
     * Registry Service API: validate the provided institution name
     *
     * This will not validate the whole prefix registration request, only the 'organization' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest validateInstitutionName(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateInstitutionName"), payload);
    }

    public ServiceResponseValidateRequest validateInstitutionHomeUrl(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateInstitutionHomeUrl"), payload);
    }

    public ServiceResponseValidateRequest validateInstitutionDescription(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateInstitutionDescription"), payload);
    }

    public ServiceResponseValidateRequest validateInstitutionLocation(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateInstitutionLocation"), payload);
    }






    /**
     * Registry Service API: validate the requested prefix.
     *
     * This will not validate the whole prefix registration request, only the 'preferred prefix' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest validateRequestedPrefix(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateRequestedPrefix"), payload);
    }

    /**
     * Registry Service API: validate 'resource access rule' field for a prefix registration request.
     *
     * This will not validate the whole prefix registration request, only the 'resource access rule' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest requestValidationResourceAccessRule(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateRegisterPrefixResourceAccessRule"), payload);
    }

    /**
     * Registry Service API: validate 'example identifier' field for a prefix registration request.
     *
     * This will not validate the whole prefix registration request, only the 'example identifier' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest requestValidationExampleIdentifier(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateRegisterPrefixExampleIdentifier"), payload);
    }

    /**
     * Registry Service API: validate 'regex pattern' field for a prefix registration request.
     *
     * This will not validate the whole prefix registration request, only the 'regex pattern' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest requestValidationRegexPattern(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateRegisterPrefixRegexPattern"), payload);
    }

    /**
     * Registry Service API: validate 'references' field for a prefix registration request.
     *
     * This will not validate the whole prefix registration request, only the 'references' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest requestValidationReferences(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateRegisterPrefixReferences"), payload);
    }

    /**
     * Registry Service API: validate 'additional information' field for a prefix registration request.
     *
     * This will not validate the whole prefix registration request, only the 'additional information' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest requestValidationAdditionalInformation(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateRegisterPrefixAdditionalInformation"), payload);
    }

    /**
     * Registry Service API: validate 'requester' field for a prefix registration request.
     *
     * This will not validate the whole prefix registration request, only the 'requester' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponseValidateRequest requestValidationRequester(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateRegisterPrefixRequester"), payload);
    }

}
