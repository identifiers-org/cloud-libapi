package org.identifiers.cloud.libapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.identifiers.cloud.libapi.models.ConfigurationException;
import org.identifiers.cloud.libapi.models.RestTemplateErrorHandlerLogError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.util.*;

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
    public static Logger logger = LoggerFactory.getLogger(Configuration.class);
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
        RESOLVER("resolver", "Compact ID Resolution Web Service"),
        METADATA("metadata", "Metadata Web Service"),
        REGISTRY("registry", "Registry Web Service");
        // As you can see, THERE IS NO key for the Resource Recommender service, as it is not offered to the public
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
    private static Map<String, Map<String, String>> servicesMap = null;

    private static void loadServicesMap() throws ConfigurationException {
        servicesMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            // TODO
            TypeReference<HashMap<String, HashMap<String, String>>> servicesMapTypeRef
                    = new TypeReference<HashMap<String, HashMap<String, String>>>() {};
            servicesMap = mapper.readValue(Configuration.class
                    .getClassLoader()
                    .getResourceAsStream("deployments.yml"), servicesMapTypeRef);
        } catch (IOException e) {
            String errorMessage = String.format("An ERROR occurred while loading deployment information!, error -> '%s'", e.getMessage());
            logger.error(errorMessage);
            throw new ConfigurationException(errorMessage);
        }
        // Check the map is correct
        Arrays.stream(InfrastructureDeploymentSelector.values()).forEach(deploymentKey -> {
            if (deploymentKey.getKey() != InfrastructureDeploymentSelector.ANY.getKey()) {
                if (!servicesMap.containsKey(deploymentKey.getKey())) {
                    String errorMessage = String.format("MISSING deployment '%s', '%s'",
                            deploymentKey.getKey().toUpperCase(),
                            deploymentKey.getDescription());
                    logger.error(errorMessage);
                    throw new ConfigurationException(errorMessage);
                }
                Arrays.stream(ServiceName.values()).forEach(serviceName -> {
                    if (!servicesMap.get(deploymentKey.getKey()).containsKey(serviceName.getName())) {
                        String errorMessage = String.format("MISSING Service '%s', '%s', in Deployment '%s', '%s'",
                                serviceName.getName(),
                                serviceName.getDescription(),
                                deploymentKey.getKey().toUpperCase(),
                                deploymentKey.getDescription());
                        logger.error(errorMessage);
                        throw new ConfigurationException(errorMessage);
                    }
                });
            }
        });
        logger.info("Deployment information LOADED!");
    }

    public static synchronized void selectDeployment(InfrastructureDeploymentSelector selector) {
        deploymentSelection = selector;
    }

    public static String getServiceLocation(ServiceName serviceName) throws ConfigurationException {
        if (servicesMap == null) {
            loadServicesMap();
        }
        if (servicesMap.keySet().isEmpty()) {
            String errorMessage = "SERVICES MAP IS EMPTY, there is no information on services deployments!";
            logger.error(errorMessage);
            throw new ConfigurationException(errorMessage);
        }
        // Default deployment to be used
        String deploymentKey = deploymentSelection.getKey();
        if (deploymentSelection.getKey() == InfrastructureDeploymentSelector.ANY.getKey()) {
            List<String> deployments = new ArrayList<>(servicesMap.keySet());
            Collections.shuffle(deployments);
            deploymentKey = deployments.get(0);
        }
        return servicesMap.get(deploymentKey).get(serviceName.name);
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
