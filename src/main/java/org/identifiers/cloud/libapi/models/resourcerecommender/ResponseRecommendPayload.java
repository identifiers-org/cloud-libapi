package org.identifiers.cloud.libapi.models.resourcerecommender;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.ResourceRecommender
 * Timestamp: 2018-03-06 12:13
 * ---
 *
 * As a result to a recommendation requests on the Resource Recommender service, a list of recommendations for all the
 * 'resolved resources' that were analized is produced. And this will be part of the response from the Resource
 * Recommender service.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseRecommendPayload implements Serializable {
    List<ResourceRecommendation> resourceRecommendations;

    public List<ResourceRecommendation> getResourceRecommendations() {
        return resourceRecommendations;
    }

    public ResponseRecommendPayload setResourceRecommendations(List<ResourceRecommendation> resourceRecommendations) {
        this.resourceRecommendations = resourceRecommendations;
        return this;
    }
}
