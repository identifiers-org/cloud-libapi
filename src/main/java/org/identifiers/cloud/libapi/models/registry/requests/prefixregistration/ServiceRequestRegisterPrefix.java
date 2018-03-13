package org.identifiers.cloud.libapi.models.registry.requests.prefixregistration;

import org.identifiers.cloud.libapi.models.ServiceRequest;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.registry.requests.prefixregistration
 * Timestamp: 2018-03-08 5:44
 * ---
 *
 * This class models a service request, for registering a new prefix, to the registry service, by specializing the
 * ServiceRequest class with the right payload.
 */
public class ServiceRequestRegisterPrefix extends ServiceRequest<ServiceRequestRegisterPrefixPayload> {
}