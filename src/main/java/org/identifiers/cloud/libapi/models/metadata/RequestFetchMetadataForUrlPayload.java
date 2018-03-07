package org.identifiers.cloud.libapi.models.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.metadata
 * Timestamp: 2018-02-17 20:27
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestFetchMetadataForUrlPayload {
    private String url;

    public String getUrl() {
        return url;
    }

    public RequestFetchMetadataForUrlPayload setUrl(String url) {
        this.url = url;
        return this;
    }
}
