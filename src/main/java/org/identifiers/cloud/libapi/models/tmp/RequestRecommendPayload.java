package org.identifiers.cloud.libapi.models.tmp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.ResourceRecommender
 * Timestamp: 2018-03-06 12:01
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRecommendPayload implements Serializable {
    private List<ResolvedResource> resolvedResources;

    public List<ResolvedResource> getResolvedResources() {
        return resolvedResources;
    }

    public RequestRecommendPayload setResolvedResources(List<ResolvedResource> resolvedResources) {
        this.resolvedResources = resolvedResources;
        return this;
    }
}
