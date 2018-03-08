package org.identifiers.cloud.libapi.models.registry.responses.prefixregistration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.registry.responses.prefixregistration
 * Timestamp: 2018-03-08 5:42
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceResponseRegisterPrefixPayload implements Serializable {
    private String comment = "No comments on your prefix registration request";

    public String getComment() {
        return comment;
    }

    public ServiceResponseRegisterPrefixPayload setComment(String comment) {
        this.comment = comment;
        return this;
    }
}
