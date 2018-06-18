package org.identifiers.cloud.libapi.models.linkchecker.requests;

import java.io.Serializable;

/**
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.linkchecker.requests
 * Timestamp: 2018-06-18 9:31
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class ScoringRequestPayload implements Serializable {
    protected String url;

    public String getUrl() {
        return url;
    }

    public ScoringRequestPayload setUrl(String url) {
        this.url = url;
        return this;
    }
}