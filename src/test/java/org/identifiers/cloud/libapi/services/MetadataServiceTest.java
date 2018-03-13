package org.identifiers.cloud.libapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.identifiers.cloud.libapi.models.metadata.ServiceResponseFetchMetadata;
import org.identifiers.cloud.libapi.models.metadata.ServiceResponseFetchMetadataForUrl;
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
 * Timestamp: 2018-03-07 15:34
 * ---
 */
public class MetadataServiceTest {
    private static Logger logger = LoggerFactory.getLogger(ResolverServiceTest.class);
    // Again, this unit tests are a simple way of human validation of the client, as there is no test data at the
    // current iteration of this library
    @Test
    public void getMetadataForCompactId() {
        ServiceResponseFetchMetadata response = ApiServicesFactory
                .getMetadataService("localhost", "8082")
                .getMetadataForCompactId("CHEBI:36927");
        // Just for debugging purposes, serialized response into the logs
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info("Test request metadata for Compact ID, " +
                    "response from the service:\n{}", mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            // Ignore
        }
        assertThat("Response from service is not OK, as there is no metadata for that Compact ID",
                response.getHttpStatus() == HttpStatus.OK,
                is(false));
    }

    @Test
    public void getMetadataForUrl() {
        ServiceResponseFetchMetadataForUrl response = ApiServicesFactory
                .getMetadataService("localhost", "8082")
                .getMetadataForUrl("http://reactome.org/content/detail/R-HSA-201451");
        // Just for debugging purposes, serialized response into the logs
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info("Test request metadata for URL, " +
                    "response from the service:\n{}", mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            // Ignore
        }
        assertThat("Response from service is OK, as there is metadata at that URL",
                response.getHttpStatus() == HttpStatus.OK,
                is(true));
    }
}