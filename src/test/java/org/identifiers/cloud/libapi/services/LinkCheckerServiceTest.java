package org.identifiers.cloud.libapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.identifiers.cloud.libapi.models.linkchecker.responses.ServiceResponseScoringRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-06-18 12:07
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class LinkCheckerServiceTest {
    private static Logger logger = LoggerFactory.getLogger(LinkCheckerServiceTest.class);
    // Service location
    private static final String serviceHost = "localhost";
    private static final String servicePort = "8084";
    // This unit tests are a simple way of human validation of the client, as there is no test data at the current
    // iteration of this library

    public void testReliabilityScoringForProvider() {
        String providerId = "MIR:00100593";
        String url = "http://www.molbase.com/";
        ServiceResponseScoringRequest response = ApiServicesFactory
                .getLinkCheckerService(serviceHost, servicePort)
                .getScoreForProvider(providerId, url);
        logger.info("Reliability score for provider ID #{}, URL '{}' ---> '{}'",
                providerId, url, response.getPayload().getScore());
        // Just for debugging purposes, serialized response into the logs
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info("Test request link checker, response from the service:\n{}",
                    mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            // Ignore
        }
        assertThat("Response from service is OK",
                response.getHttpStatus() == HttpStatus.OK,
                is(true));
    }
}
