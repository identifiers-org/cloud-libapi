package org.identifiers.cloud.libapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models
 * Timestamp: 2018-03-06 11:32
 * ---
 *
 * This is a generic class that models the common parts to any service responses, e.g. the api version.
 *
 * It is specialized with the payload that corresponds to every particular service response.
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"httpStatus"})
public class ServiceResponse<T> implements Serializable {
    private String apiVersion;
    private String errorMessage;
    private HttpStatus httpStatus = HttpStatus.OK;
    // payload
    private T payload;

    public String getApiVersion() {
        return apiVersion;
    }

    public ServiceResponse setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ServiceResponse setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ServiceResponse setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public T getPayload() {
        return payload;
    }

    public ServiceResponse setPayload(T payload) {
        this.payload = payload;
        return this;
    }
}
