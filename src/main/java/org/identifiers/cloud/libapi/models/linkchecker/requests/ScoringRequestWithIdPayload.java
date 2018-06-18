package org.identifiers.cloud.libapi.models.linkchecker.requests;

import java.io.Serializable;

/**
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.linkchecker.requests
 * Timestamp: 2018-06-18 9:34
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
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
