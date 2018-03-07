package org.identifiers.cloud.libapi.models.resolver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.resolver
 * Timestamp: 2018-03-07 7:40
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseResolvePayload {
    private List<ResolvedResource> resolvedResources;

    public List<ResolvedResource> getResolvedResources() {
        return resolvedResources;
    }

    public ResponseResolvePayload setResolvedResources(List<ResolvedResource> resolvedResources) {
        this.resolvedResources = resolvedResources;
        return this;
    }
}
