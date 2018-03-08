package org.identifiers.cloud.libapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.identifiers.cloud.libapi.ApiServicesFactory;
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
                        .setHomePage("http://identifiers.org")
                        .setOrganization("EMBL-EBI")
                        .setPreferredPrefix("mynewprefix")
                        .setResourceAccessRule("http://httpstat.us/{$id}")
                        .setExampleIdentifier("200")
                        .setRegexPattern("\\d+")
                        .setReferences(new String[]{"ref1", "ref2"})
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
                        .requestValidationName(payload);
        checkResultOk(response, "Validation Request - Name");
    }

    @Test
    public void requestValidateDescription() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .requestValidationDescription(payload);
        checkResultOk(response, "Validation Request - Description");
    }

    @Test
    public void requestValidateHomePage() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .requestValidationHomePage(payload);
        checkResultOk(response, "Validation Request - HomePage");
    }

    @Test
    public void requestValidateOrganization() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .requestValidationOrganization(payload);
        checkResultOk(response, "Validation Request - Organization");
    }


}