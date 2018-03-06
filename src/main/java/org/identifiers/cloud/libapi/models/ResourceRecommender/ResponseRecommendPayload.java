package org.identifiers.cloud.libapi.models.ResourceRecommender;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.ResourceRecommender
 * Timestamp: 2018-03-06 12:13
 * ---
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
