package org.identifiers.cloud.libapi.models.metadata;

import org.identifiers.cloud.libapi.models.ServiceRequest;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.metadata
 * Timestamp: 2018-03-07 13:17
 * ---
 *
 * This class models a metadata request, to the metadata service, for a given URL, by specializing the ServiceRequest
 * class with the right payload
 */
public class ServiceRequestFetchMetadataForUrl extends ServiceRequest<RequestFetchMetadataForUrlPayload> {
}
