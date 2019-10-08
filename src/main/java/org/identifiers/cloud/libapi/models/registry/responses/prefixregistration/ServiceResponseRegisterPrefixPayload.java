package org.identifiers.cloud.libapi.models.registry.responses.prefixregistration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.registry.responses.prefixregistration
 * Timestamp: 2018-03-08 5:42
 * ---
 *
 * This class models the payload information within a prefix registration request response, from the registry service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceResponseRegisterPrefixPayload implements Serializable {
    // Comments on the prefix registration request
    private String comment = "No comments on your prefix registration request";
    // This is an ephemeral token whose lifecycle is tight to the lifecycle of the prefix registration requests, so,
    // once the prefix registration has been solved, either rejected or accepted, this token is no longer valid.
    private String token = "";
}
