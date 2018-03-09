package org.identifiers.cloud.libapi;

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

    private void testAnyResolver(Configuration.ServiceName serviceName) {
        Configuration.selectDeployment(Configuration.InfrastructureDeploymentSelector.ANY);
        Set<String> serviceLocations = IntStream.range(0, 10).mapToObj(i -> {
            return Configuration.getServiceLocation(serviceName);
        }).collect(Collectors.toSet());
        assertThat("When ANY, we get multiple locations for a service",
                serviceLocations.size() > 1,
                is(true));
    }
}