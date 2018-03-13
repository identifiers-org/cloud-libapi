package org.identifiers.cloud.libapi.models.registry.requests.validation;

import org.identifiers.cloud.libapi.models.ServiceRequest;
import org.identifiers.cloud.libapi.models.registry.requests.prefixregistration.ServiceRequestRegisterPrefixPayload;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.registry.requests.validation
 * Timestamp: 2018-03-08 6:11
 * ---
 *
 * This class models a validation request, to the registry service, by specializing the ServiceRequest class with the
 * right payload.
 */
public class ServiceRequestValidate extends ServiceRequest<ServiceRequestRegisterPrefixPayload> {
}
