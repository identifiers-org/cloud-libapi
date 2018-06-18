package org.identifiers.cloud.libapi.models.linkchecker.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.linkchecker.requests
 * Timestamp: 2018-06-18 9:34
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScoringRequestWithIdPayload extends ScoringRequestPayload implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public ScoringRequestWithIdPayload setId(String id) {
        this.id = id;
        return this;
    }
}
