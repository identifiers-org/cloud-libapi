package org.identifiers.cloud.libapi.models.resourcerecommender;

import org.identifiers.cloud.libapi.models.ServiceRequest;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.ResourceRecommender
 * Timestamp: 2018-03-06 12:04
 * ---
 *
 * This class models a recommendation request, for the Resource Recommender service, by specializing the ServiceRequest
 * class with the right payload.
 */
public class ServiceRequestRecommend extends ServiceRequest<RequestRecommendPayload> {
}
