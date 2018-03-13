package org.identifiers.cloud.libapi.models.registry.responses.validation;

import org.identifiers.cloud.libapi.models.ServiceResponse;
import org.identifiers.cloud.libapi.models.registry.responses.prefixregistration.ServiceResponseRegisterPrefixPayload;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.registry.responses.validation
 * Timestamp: 2018-03-08 6:12
 * ---
 *
 * This class models the registry service response to a validation request, by specializing the ServiceResponse class
 * with the right payload.
 */
public class ServiceResponseValidateRequest extends ServiceResponse<ServiceResponseRegisterPrefixPayload> {
}
