package org.identifiers.cloud.libapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models
 * Timestamp: 2018-03-06 11:28
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ServiceRequest<T> implements Serializable {
    private String apiVersion;
    private T payload;

    public String getApiVersion() {
        return apiVersion;
    }

    public ServiceRequest setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public T getPayload() {
        return payload;
    }

    public ServiceRequest setPayload(T payload) {
        this.payload = payload;
        return this;
    }
}
