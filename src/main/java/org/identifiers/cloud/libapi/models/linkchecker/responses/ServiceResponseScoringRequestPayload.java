package org.identifiers.cloud.libapi.models.linkchecker.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.linkchecker.responses
 * Timestamp: 2018-06-18 9:44
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceResponseScoringRequestPayload implements Serializable {
    // Default scoring
    private int score = 50;

    public int getScore() {
        return score;
    }

    public ServiceResponseScoringRequestPayload setScore(int score) {
        this.score = score;
        return this;
    }
}
