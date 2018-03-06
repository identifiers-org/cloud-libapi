package org.identifiers.cloud.libapi.models.ResourceRecommender;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.ResourceRecommender
 * Timestamp: 2018-03-06 11:58
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResolvedResource implements Serializable {
    // Even if we have access to another service that, given a resource ID, could provide information on that resource,
    // these particular attributes exist within the context that resource / provider for a particular Compact ID. In the
    // future, we could include more context information related to the particularities of the current recommendation
    // request to fine tune the recommendation mechanism

    // This field references the ID of the resource within the context of the current Compact ID resolved request
    private String id;
    // This field references the final URL that points to the current resolved resource request
    private String accessURL;
    // For this particular resolved resource request, provides information on whether the resource is official or not
    private boolean official;

    public String getId() {
        return id;
    }

    public ResolvedResource setId(String id) {
        this.id = id;
        return this;
    }

    public String getAccessURL() {
        return accessURL;
    }

    public ResolvedResource setAccessURL(String accessURL) {
        this.accessURL = accessURL;
        return this;
    }

    public boolean isOfficial() {
        return official;
    }

    public ResolvedResource setOfficial(boolean official) {
        this.official = official;
        return this;
    }
}