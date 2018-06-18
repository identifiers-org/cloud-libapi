package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.support.RetryTemplate;

/**
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-06-18 9:47
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This class implements a client for the Link Checker Service.
 */
public class LinkCheckerService {
    public static final String apiVersion = "1.0";
    private static final Logger logger = LoggerFactory.getLogger(LinkCheckerService.class);
    // Re-try pattern
    private RetryTemplate retryTemplate = Configuration.retryTemplate();
    private String serviceApiBaseline;

    public LinkCheckerService() {
    }

    // TODO
}
