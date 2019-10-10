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
                        .setProviderDescription("Identifiers.org platform")
                        .setProviderCode("idorg")
                        .setProviderHomeUrl("http://identifiers.org")
                        .setProviderLocation("GB")
                        .setInstitutionName("EMBL-EBI")
                        .setInstitutionHomeUrl("https://www.ebi.ac.uk")
                        .setInstitutionDescription("The European Bioinformatics Institute, blah, blah, blah...")
                        .setInstitutionLocation("GB")
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
    public void testPrefixRegistration() {
        ServiceResponseRegisterPrefix response =
                ApiServicesFactory.getRegistryService("localhost", "8081").setProtocolSchemeToHttp()
                        .requestPrefixRegistration(payload);
        checkResultOk(response, "Prefix Registration Request");
    }

    @Test
    public void testNamespaceNameValidation() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081").setProtocolSchemeToHttp()
                        .validateName(payload);
        checkResultOk(response, "Validation Request - Name");
    }

    @Test
    public void testNamespaceDescriptionValidation() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081").setProtocolSchemeToHttp()
                        .validateDescription(payload);
        checkResultOk(response, "Validation Request - Description");
    }

    @Test
    public void testValidateProviderHomeUrl() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081").setProtocolSchemeToHttp()
                        .validateProviderHomeUrl(payload);
        checkResultOk(response, "Validation Request - Provider home URL");
    }

    @Test
    public void testValidateProviderName() {
        ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081").setProtocolSchemeToHttp()
                        .validateProviderName(payload);
        checkResultOk(response, "Validation Request - Provider name");
    }


}