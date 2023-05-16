package org.identifiers.cloud.libapi.models.resourcerecommender;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.ResourceRecommender
 * Timestamp: 2018-03-06 11:58
 * ---
 *
 * This POJO models the concept of a "resolved resource" for the recommender service, i.e. a resource that has been
 * found candidate to provide information on a given Compact ID.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResolvedResource implements Serializable {
    // Even if we have access to another service that, given a resource ID, could provide information on that resource,
    // these particular attributes exist within the context that resource / provider for a particular Compact ID. In the
    // future, we could include more context information related to the particularities of the current recommendation
    // request to fine tune the recommendation mechanism

    // This field references the ID of the resource within the context of the current Compact ID resolved request
    private String id;
    // Resource MIR ID
    private String mirId;
    // This is the resolved URL for the given compact identifier
    private String compactIdentifierResolvedUrl;
    // Location information for this resource
    private Location location;
    // Whether this resource is official or not
    private boolean official;
    // Home URL for this resource within the context of the namespace where it's registered
    private String resourceHomeUrl;
    // Deprecation information
    private String namespacePrefix;
    private boolean deprecatedNamespace = false;
    private Date namespaceDeprecationDate;
    private boolean deprecatedResource = false;
    private Date resourceDeprecationDate;
    private boolean protectedUrls;
    private boolean renderProtectedLanding;
    private String authHelpUrl;
    private String authHelpDescription;
}