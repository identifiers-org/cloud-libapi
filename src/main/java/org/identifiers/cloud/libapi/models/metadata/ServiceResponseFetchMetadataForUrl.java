package org.identifiers.cloud.libapi.models.metadata;

import org.identifiers.cloud.libapi.models.ServiceResponse;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.metadata
 * Timestamp: 2018-03-07 13:18
 * ---
 *
 * This class models the response from the metadata service to a metadata request for a given URL, by specializing the
 * ServiceResponse class with the right payload.
 */
public class ServiceResponseFetchMetadataForUrl extends ServiceResponse<ResponseFetchMetadataForUrlPayload> {
}
