package org.identifiers.cloud.libapi.models.resolver;

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
 * Package: org.identifiers.cloud.libapi.models.resolver
 * Timestamp: 2018-03-07 7:31
 * ---
 *
 * This class models a recommendation score, with a posisble explanation, for a particular resource in the response from
 * the Resolver service, when the resolution of a Comapct ID has been requested.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recommendation implements Serializable {
    // This models a recommendation attached to a particular resource in the response from the Resolver Web Service
    private int recommendationIndex = 0;
    private String recommendationExplanation = "--- default explanation ---";
}
