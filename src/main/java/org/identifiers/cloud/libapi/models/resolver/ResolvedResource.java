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
 * Timestamp: 2018-03-07 7:28
 * ---
 *
 * This POJO models a Compact ID information provider, as result of a resolution request to the Resolver for a
 * particular Compact ID.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResolvedResource implements Serializable {
    // identifiers.org internal ID for this resource
    private String id;
    // Provider code for this resource
    private String providerCode;
    // This is the resolved URL for the given compact identifier
    private String compactIdentifierResolvedUrl;
    // A description of this resource
    private String description;
    // The institution this resource belongs to
    private Institution institution;
    // Location information for this resource
    private Location location;
    // Whether this resource is official or not
    private boolean official;
    // Home URL for this resource within the context of the namespace where it's registered
    private String resourceHomeUrl;
    // Recommendation scoring information
    private Recommendation recommendation;
}
