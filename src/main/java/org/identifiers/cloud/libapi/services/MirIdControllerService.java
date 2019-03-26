package org.identifiers.cloud.libapi.services;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.libapi.Configuration;
import org.springframework.retry.support.RetryTemplate;

/**
 * Project: cloud-libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2019-03-26 10:49
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is an implementation of a client for the MIR ID Controller API Service
 */
@Slf4j
public class MirIdControllerService {

    private RetryTemplate retryTemplate = Configuration.retryTemplate();
    private String serviceApiBaseline;

    public MirIdControllerService(String serviceHost, String servicePort) {
        // TODO - This needs to be refactored in the future for supporting multiple schema (HTTP, HTTPS)
        this.serviceApiBaseline = String.format("http://%s:%s", serviceHost, servicePort);
    }
}
