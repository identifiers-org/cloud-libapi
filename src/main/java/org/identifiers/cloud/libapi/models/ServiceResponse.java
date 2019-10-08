package org.identifiers.cloud.libapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"httpStatus"})
public class ServiceResponse<T> implements Serializable {
    private String apiVersion;
    private String errorMessage;
    private HttpStatus httpStatus = HttpStatus.OK;
    // payload
    private T payload;
}
