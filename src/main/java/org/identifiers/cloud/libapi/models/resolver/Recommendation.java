package org.identifiers.cloud.libapi.models.resolver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.resolver
 * Timestamp: 2018-03-07 7:31
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recommendation implements Serializable {
    // This models a recommendation attached to a particular resource in the response from the Resolver Web Service
    private int recommendationIndex = 0;
    private String recommendationExplanation = "--- default explanation ---";

    public int getRecommendationIndex() {
        return recommendationIndex;
    }

    public Recommendation setRecommendationIndex(int recommendationIndex) {
        this.recommendationIndex = recommendationIndex;
        return this;
    }

    public String getRecommendationExplanation() {
        return recommendationExplanation;
    }

    public Recommendation setRecommendationExplanation(String recommendationExplanation) {
        this.recommendationExplanation = recommendationExplanation;
        return this;
    }
}
