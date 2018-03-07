package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;
import org.identifiers.cloud.libapi.models.resolver.ResponseResolvePayload;
import org.identifiers.cloud.libapi.models.resolver.ServiceResponseResolve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;

import java.util.ArrayList;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-07 10:04
 * ---
 */
public class ResolverService {
    public static final String apiVersion = "1.0";
    private static final Logger logger = LoggerFactory.getLogger(ResolverService.class);
    // Re-try pattern, externalize this later if needed
    private RetryTemplate retryTemplate = Configuration.retryTemplate();
    private String serviceApiBaseline;

    public ResolverService(String host, String port) {
        serviceApiBaseline = String.format("http://%s:%s", host, port);
    }

    private ServiceResponseResolve createDefaultResponse(HttpStatus httpStatus, String errorMessage) {
        ServiceResponseResolve response = new ServiceResponseResolve();
        response
                .setApiVersion(apiVersion)
                .setHttpStatus(httpStatus)
                .setErrorMessage(errorMessage);
        response.setPayload(new ResponseResolvePayload().setResolvedResources(new ArrayList<>()));
        return response;
    }

    public ServiceResponseResolve requestCompactIdResolution(String compactId) {
        // TODO
        String serviceApiEndpoint = serviceApiBaseline;
        ServiceResponseResolve response = createDefaultResponse(HttpStatus.OK, "");
    }
}
