package org.identifiers.cloud.libapi.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models
 * Timestamp: 2018-03-07 4:09
 * ---
 *
 * For the different clients (service wrappers), this error handler is used. It's purpose is to log the error but allow
 * the caller to get the response, so the HTTP status code can be accessed.
 */
public class RestTemplateErrorHandlerLogError implements ResponseErrorHandler {
    private static Logger logger = LoggerFactory.getLogger(RestTemplateErrorHandlerLogError.class);
    ClientHttpResponse clientHttpResponse;

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        // We're going to say that it has no error so we can't handle this properly
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        logger.error("The following error came back from the Recommender Service, HTTP Status #{}, error content '{}'",
                clientHttpResponse.getRawStatusCode(),
                clientHttpResponse.getStatusText());
    }
}