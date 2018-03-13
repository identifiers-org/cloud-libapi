package org.identifiers.cloud.libapi.models.metadata;

import org.identifiers.cloud.libapi.models.ServiceResponse;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.metadata
 * Timestamp: 2018-03-07 11:01
 * ---
 *
 * This class models a response from the metadata service to a metadata request for a Compact ID, by specializing the
 * ServiceResponse class with the right payload.
 */
public class ServiceResponseFetchMetadata extends ServiceResponse<ResponseFetchMetadataPayload> {
}
