package org.identifiers.cloud.libapi;

import org.identifiers.cloud.libapi.models.RestTemplateErrorHandlerLogError;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi
 * Timestamp: 2018-03-06 11:36
 * ---
 */
public class Configuration {
    public static final int WS_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    public static final int WS_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds
    public enum InfrastructureDeploymentSelector {
        AWS("aws", "Amazon Web Services deployment"),
        GCLOUD("gcloud", "Google Cloud deployment"),
        AZURE("azure", "Microsoft Azure deployment"),
        ANY("any", "Any deployment");

        private String key;
        private String description;

        InfrastructureDeploymentSelector(String key, String description) {
            this.key = key;
            this.description = description;
        }

        public String getKey() {
            return key;
        }

        public InfrastructureDeploymentSelector setKey(String key) {
            this.key = key;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public InfrastructureDeploymentSelector setDescription(String description) {
            this.description = description;
            return this;
        }
    }
    public enum ServiceName {
        ;
        private String name;
        private String description;

        ServiceName(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public ServiceName setName(String name) {
            this.name = name;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public ServiceName setDescription(String description) {
            this.description = description;
            return this;
        }
    }
    public static InfrastructureDeploymentSelector deploymentSelection = InfrastructureDeploymentSelector.ANY;

    public static void selectDeployment(InfrastructureDeploymentSelector selector) {
        deploymentSelection = selector;
    }

    public static RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(WS_REQUEST_RETRY_MAX_ATTEMPTS);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(WS_REQUEST_RETRY_BACK_OFF_PERIOD);

        retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

    public static ResponseErrorHandler responseErrorHandler() {
        return new RestTemplateErrorHandlerLogError();
    }

}
