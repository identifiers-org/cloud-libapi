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
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"httpStatus"})
public class ServiceResponse<T> implements Serializable {
    private String errorMessage;
    private HttpStatus httpStatus = HttpStatus.OK;
    // payload
    private T payload;
}
