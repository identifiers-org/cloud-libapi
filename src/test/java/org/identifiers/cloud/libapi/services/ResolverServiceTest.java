package org.identifiers.cloud.libapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.libapi.models.resolver.ServiceResponseResolve;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-07 10:35
 * ---
 */
@Slf4j
public class ResolverServiceTest {
    // Again, this unit tests are a simple way of human validation of the client, as there is no test data at the
    // current iteration of this library
    @Test
    public void requestCompactIdResolution() {
        ServiceResponseResolve response = ApiServicesFactory
                .getResolverService("localhost", "8080")
                .requestCompactIdResolution("CHEBI:36927");
        // Just for debugging purposes, serialized response into the logs
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info("Test request resolver, response from the service:\n{}", mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            // Ignore
        }
        assertThat("Response from service is OK",
                response.getHttpStatus() == HttpStatus.OK,
                is(true));
    }

    @Test
    public void requestCompactIdResolutionWithSelector() {
        ServiceResponseResolve response = ApiServicesFactory
                .getResolverService("localhost", "8080")
                .requestCompactIdResolution("CHEBI:36927", "ebi");
        // Just for debugging purposes, serialized response into the logs
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info("Test request resolver, response from the service:\n{}", mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            // Ignore
        }
        assertThat("Response from service is OK",
                response.getHttpStatus() == HttpStatus.OK,
                is(true));
    }

    @Test
    public void testResolutionWithRawRequest() {
        ServiceResponseResolve response = ApiServicesFactory
                .getResolverService("localhost", "8080")
                .requestResolutionRawRequest("ark:/57799/b97957");
        // Just for debugging purposes, serialized response into the logs
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info("Test request resolver, response from the service:\n{}", mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            // Ignore
        }
        assertThat("Response from service is OK",
                response.getHttpStatus() == HttpStatus.OK,
                is(true));
    }

    @Test
    public void getAllResourcesResolvedToTheirSampleIds() {
        ServiceResponseResolve response = ApiServicesFactory
                .getResolverService("localhost", "8080")
                .getAllSampleIdsResolved();
        // Just for debugging purposes, serialized response into the logs
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info("Test request resolver, response from the service:\n{}", mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            // Ignore
        }
        assertThat("Response from service is OK",
                response.getHttpStatus() == HttpStatus.OK,
                is(true));
    }

    @Test
    public void getAllResourcesHomeUrls() {
        ServiceResponseResolve response = ApiServicesFactory
                .getResolverService("localhost", "8080")
                .getAllHomeUrls();
        // Just for debugging purposes, serialized response into the logs
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info("Test request resolver, response from the service:\n{}", mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            // Ignore
        }
        assertThat("Response from service is OK",
                response.getHttpStatus() == HttpStatus.OK,
                is(true));
    }
}