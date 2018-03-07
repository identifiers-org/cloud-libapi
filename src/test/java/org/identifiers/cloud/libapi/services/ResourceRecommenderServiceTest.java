package org.identifiers.cloud.libapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.identifiers.cloud.libapi.ApiServicesFactory;
import org.identifiers.cloud.libapi.models.tmp.ResolvedResource;
import org.identifiers.cloud.libapi.models.tmp.ServiceResponseRecommend;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-06 16:36
 * ---
 */
public class ResourceRecommenderServiceTest {
    private static Logger logger = LoggerFactory.getLogger(ResourceRecommenderServiceTest.class);

    @Test
    public void requestRecommendations() {
        // TODO
        // There is no testing data for this iteration of the library, so this unit test is design as a simple API
        // compliance test, where we check that the client can effectively connect the web service and pull a response
        // back from it
        List<ResolvedResource> resolvedResources = IntStream.range(0, 9).parallel().mapToObj(index -> {
            return new ResolvedResource()
                    .setAccessURL(String.format("http://resolved.resource/%d", index))
                    .setId(Integer.toString(index));
        }).collect(Collectors.toList());
        // Set an official one
        resolvedResources.get(0).setOfficial(true);
        // Get the service wrapper
        ResourceRecommenderService service = ApiServicesFactory
                .getResourceRecommenderService("localhost", "8083");
        // Call the service
        ServiceResponseRecommend response = service.requestRecommendations(resolvedResources);
        // Just for debugging purposes, serialized response into the logs
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info("Test request recommendation, response from the service:\n{}", mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            // Ignore
        }
        assertThat("Response from service is OK",
                response.getHttpStatus() == HttpStatus.OK,
                is(true));
        assertThat("We get the same number of resources back",
                response.getPayload().getResourceRecommendations().size() == resolvedResources.size(),
                is(true));
    }
}