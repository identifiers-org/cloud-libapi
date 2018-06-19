package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.models.linkchecker.responses.ServiceResponseScoringRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        // TODO
    }
}
