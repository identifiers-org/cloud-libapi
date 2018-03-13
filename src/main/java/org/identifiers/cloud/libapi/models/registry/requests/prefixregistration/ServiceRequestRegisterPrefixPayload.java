package org.identifiers.cloud.libapi.models.registry.requests.prefixregistration;

import org.identifiers.cloud.libapi.models.registry.Requester;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.registry.requests.prefixregistration
 * Timestamp: 2018-01-30 17:47
 * ---
 *
 * This class models the payload used when interacting with the registry service for registration and validation
 * requests.
 *
 * It's a basic POJO with self explanatory fields.
 */
public class ServiceRequestRegisterPrefixPayload implements Serializable {
    private String name;
    private String description;
    private String homePage;
    private String organization;
    private String preferredPrefix;
    private String resourceAccessRule;
    private String exampleIdentifier;
    private String regexPattern;
    private String[] references;
    private String additionalInformation;
    private Requester requester;

    public String getName() {
        return name;
    }

    public ServiceRequestRegisterPrefixPayload setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ServiceRequestRegisterPrefixPayload setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getHomePage() {
        return homePage;
    }

    public ServiceRequestRegisterPrefixPayload setHomePage(String homePage) {
        this.homePage = homePage;
        return this;
    }

    public String getOrganization() {
        return organization;
    }

    public ServiceRequestRegisterPrefixPayload setOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    public String getPreferredPrefix() {
        return preferredPrefix;
    }

    public ServiceRequestRegisterPrefixPayload setPreferredPrefix(String preferredPrefix) {
        this.preferredPrefix = preferredPrefix;
        return this;
    }

    public String getResourceAccessRule() {
        return resourceAccessRule;
    }

    public ServiceRequestRegisterPrefixPayload setResourceAccessRule(String resourceAccessRule) {
        this.resourceAccessRule = resourceAccessRule;
        return this;
    }

    public String getExampleIdentifier() {
        return exampleIdentifier;
    }

    public ServiceRequestRegisterPrefixPayload setExampleIdentifier(String exampleIdentifier) {
        this.exampleIdentifier = exampleIdentifier;
        return this;
    }

    public String getRegexPattern() {
        return regexPattern;
    }

    public ServiceRequestRegisterPrefixPayload setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
        return this;
    }

    public String[] getReferences() {
        return references;
    }

    public ServiceRequestRegisterPrefixPayload setReferences(String[] references) {
        this.references = references;
        return this;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public ServiceRequestRegisterPrefixPayload setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }

    public Requester getRequester() {
        return requester;
    }

    public ServiceRequestRegisterPrefixPayload setRequester(Requester requester) {
        this.requester = requester;
        return this;
    }
}
