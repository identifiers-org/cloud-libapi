package org.identifiers.cloud.libapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.identifiers.cloud.libapi.models.ServiceResponse;
import org.identifiers.cloud.libapi.models.registry.Requester;
import org.identifiers.cloud.libapi.models.registry.requests.prefixregistration.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.libapi.models.registry.responses.prefixregistration.ServiceResponseRegisterPrefix;
import org.identifiers.cloud.libapi.models.registry.responses.validation.ServiceResponseValidateRequest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-08 12:56
 * ---
 */
public class RegistryServiceTest {
    private static Logger logger = LoggerFactory.getLogger(RegistryServiceTest.class);

    private ServiceRequestRegisterPrefixPayload payload;

    @Before
    public void setUp() {
        payload =
                new ServiceRequestRegisterPrefixPayload()
                        .setName("Unit test name")
                        .setDescription("This is a sample prefix registration request from a unit test of libapi, " +
                                "we need enouch characters for the description")
                        .setProviderName("Identifiers.org")
                        .setProviderHomeUrl("http://identifiers.org")
                        .setInstitutionName("EMBL-EBI")
                        .setRequestedPrefix("mynewprefix")
                        .setProviderUrlPattern("http://httpstat.us/{$id}")
                        .setSampleId("200")
                        .setIdRegexPattern("\\d+")
                        .setSupportingReferences(new String[]{"ref1", "ref2"})
                        .setAdditionalInformation("Additional information about this unit test")
                        .setRequester(new Requester()
                                .setEmail("mbernal@ebi.ac.uk")
                                .setName("Manuel Bernal Llinares"));
    }

    private <T> void checkResultOk(ServiceResponse<T> response, String context) {
        // Just for debugging purposes, serialized response into the logs
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info("Test '%s', " +
                    "response from the service:\n{}", context, mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            // Ignore
        }
        assertThat(String.format("Response from service is OK (%s)", context),
                response.getHttpStatus() == HttpStatus.OK,
                is(true));
    }

    @Test
    public void requestPrefixRegistration() {
        ServiceResponseRegisterPrefix response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .requestPrefixRegistration(payload);
        checkResultOk(response, "Prefix Registration Request");
    }

    @Test
    public void requestValidateName() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .validateName(payload);
        checkResultOk(response, "Validation Request - Name");
    }

    @Test
    public void requestValidateDescription() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .validateDescription(payload);
        checkResultOk(response, "Validation Request - Description");
    }

    @Test
    public void requestValidateHomePage() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .validateProviderHomeUrl(payload);
        checkResultOk(response, "Validation Request - HomePage");
    }

    @Test
    public void requestValidateOrganization() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .validateInstitutionName(payload);
        checkResultOk(response, "Validation Request - Organization");
    }

    @Test
    public void requestValidatePreferredPrefix() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .validateRequestedPrefix(payload);
        checkResultOk(response, "Validation Request - PreferredPrefix");
    }

    @Test
    public void requestValidateResourceAccessRule() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .requestValidationResourceAccessRule(payload);
        checkResultOk(response, "Validation Request - ResourceAccessRule");
    }

    @Test
    public void requestValidateExampleIdentifier() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .validateSampleId(payload);
        checkResultOk(response, "Validation Request - ExampleIdentifier");
    }

    @Test
    public void requestValidateRegexPattern() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .validateIdRegexPattern(payload);
        checkResultOk(response, "Validation Request - RegexPattern");
    }

    @Test
    public void requestValidateReferences() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .validateReferences(payload);
        checkResultOk(response, "Validation Request - References");
    }

    @Test
    public void requestValidateAdditionalInformation() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .validateAdditionalInformation(payload);
        checkResultOk(response, "Validation Request - AdditionalInformation");
    }

    @Test
    public void requestValidateRequester() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .validateRequester(payload);
        checkResultOk(response, "Validation Request - Requester");
    }

}