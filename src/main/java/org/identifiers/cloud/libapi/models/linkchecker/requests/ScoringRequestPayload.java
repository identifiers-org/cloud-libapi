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
    protected boolean accept401or403;

    public boolean isAccept401or403() {
        return accept401or403;
    }
    public ScoringRequestPayload setAccept401or403(boolean accept401or403) {
        this.accept401or403 = accept401or403;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ScoringRequestPayload setUrl(String url) {
        this.url = url;
        return this;
    }
}