package org.identifiers.cloud.libapi;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi
 * Timestamp: 2018-03-09 12:20
 * ---
 */
public class ConfigurationTest {

    private void testAnySelection(Configuration.ServiceName serviceName) {
        Configuration.selectDeployment(Configuration.InfrastructureDeploymentSelector.ANY);
        Set<String> serviceLocations = IntStream.range(0, 10).mapToObj(i -> {
            return Configuration.getServiceLocation(serviceName);
        }).collect(Collectors.toSet());
        assertThat("When ANY, we get multiple locations for a service",
                serviceLocations.size() > 1,
                is(true));
    }

    private void testSelectionLocked(Configuration.ServiceName serviceName) {
        Arrays.stream(Configuration.InfrastructureDeploymentSelector.values()).forEach(deployment -> {
            if (deployment.getKey() != Configuration.InfrastructureDeploymentSelector.ANY.getKey()) {
                Configuration.selectDeployment(deployment);
                Set<String> serviceLocations = IntStream.range(0, 10).mapToObj(i -> {
                    return Configuration.getServiceLocation(serviceName);
                }).collect(Collectors.toSet());
                assertThat("When a deployment is selected, we get only one location for a service",
                        serviceLocations.size() == 1,
                        is(true));
            }
        });
    }

    @Test
    public void testResolverLocations() {
        testAnySelection(Configuration.ServiceName.RESOLVER);
        testSelectionLocked(Configuration.ServiceName.RESOLVER);
    }

    @Test
    public void testMetadataLocations() {
        testAnySelection(Configuration.ServiceName.METADATA);
        testSelectionLocked(Configuration.ServiceName.METADATA);
    }

}