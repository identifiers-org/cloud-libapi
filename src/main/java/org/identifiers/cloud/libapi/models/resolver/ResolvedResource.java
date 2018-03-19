package org.identifiers.cloud.libapi.models.resolver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.resolver
 * Timestamp: 2018-03-07 7:28
 * ---
 *
 * This POJO models a Compact ID information provider, as result of a resolution request to the Resolver for a
 * particular Compact ID.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResolvedResource implements Serializable {
    private String id;
    private String resourcePrefix;
    private String accessUrl;
    private String info;
    private String institution;
    private String location;
    private boolean official;
    private Recommendation recommendation;

    public String getId() {
        return id;
    }

    public ResolvedResource setId(String id) {
        this.id = id;
        return this;
    }

    public String getResourcePrefix() {
        return resourcePrefix;
    }

    public ResolvedResource setResourcePrefix(String resourcePrefix) {
        this.resourcePrefix = resourcePrefix;
        return this;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public ResolvedResource setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public ResolvedResource setInfo(String info) {
        this.info = info;
        return this;
    }

    public String getInstitution() {
        return institution;
    }

    public ResolvedResource setInstitution(String institution) {
        this.institution = institution;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public ResolvedResource setLocation(String location) {
        this.location = location;
        return this;
    }

    public boolean isOfficial() {
        return official;
    }

    public ResolvedResource setOfficial(boolean official) {
        this.official = official;
        return this;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public ResolvedResource setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
        return this;
    }
}
