package org.identifiers.cloud.libapi.models.metadata;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.metadata
 * Timestamp: 2018-03-07 10:59
 * ---
 *
 * This class models the payload that is part of the metadata service response, when requesting metadata for a given
 * Compact ID
 */
public class ResponseFetchMetadataPayload implements Serializable {
    Object metadata;

    public Object getMetadata() {
        return metadata;
    }

    public ResponseFetchMetadataPayload setMetadata(Object metadata) {
        this.metadata = metadata;
        return this;
    }
}
