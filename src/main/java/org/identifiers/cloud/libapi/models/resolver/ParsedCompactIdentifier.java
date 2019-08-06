package org.identifiers.cloud.libapi.models.resolver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Project: cloud-libapi
 * Package: org.identifiers.cloud.libapi.models.resolver
 * Timestamp: 2019-08-06 14:14
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class ParsedCompactIdentifier implements Serializable {
    private String providerCode;
    // In fact, the 'prefix' value of a namespace
    private String namespace;
    private String localId;
    private String rawRequest;
    // This will flag the clients on whether this compact identifier has the namespace embedded in LUI or not.
    private boolean namespaceEmbeddedInLui = false;
    // Deprecation Information
    private boolean deprecatedNamespace = false;
    private Date namespaceDeprecationDate;
}
