package org.identifiers.cloud.libapi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-06 11:35
 * ---
 */
public class ResourceRecommenderService {
    public static final int WS_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    public static final int WS_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds

    private static final Logger logger = LoggerFactory.getLogger(ResourceRecommenderService.class);

    // Re-try pattern, externalize this later if needed
    private static final RetryTemplate retryTemplate;
    static {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(WS_REQUEST_RETRY_MAX_ATTEMPTS);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(WS_REQUEST_RETRY_BACK_OFF_PERIOD);

        retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
    }

    private String resourceRecommenderServiceHost;
    private String resourceRecommenderServicePort;

    // Error handler for the request
    class RestTemplateErrorHandler implements ResponseErrorHandler {
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
}