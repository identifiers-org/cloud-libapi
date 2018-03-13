package org.identifiers.cloud.libapi.models.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.metadata
 * Timestamp: 2018-02-17 20:27
 * ---
 *
 * When submitting a metadata request to the metadata service for a URL, this is the payload to be used, it just
 * contains the URL from which we want to obtain metadata
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestFetchMetadataForUrlPayload {
    private String url;

    public String getUrl() {
        return url;
    }

    /**
     * Use this constructor to instantiate the payload for the metadata request, given a URL
     * @param url from which metadata has to be fetched
     * @return the response from the metadata service
     */
    public RequestFetchMetadataForUrlPayload setUrl(String url) {
        this.url = url;
        return this;
    }
}
