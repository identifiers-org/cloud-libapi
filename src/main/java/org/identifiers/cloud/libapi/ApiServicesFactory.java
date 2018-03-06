package org.identifiers.cloud.libapi;

import org.identifiers.cloud.libapi.services.ResourceRecommenderService;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi
 * Timestamp: 2018-03-06 13:18
 * ---
 */
public class ApiServicesFactory {

    // TODO - Create a method to get a Resource Reommender service pointing at any of our satellite deployments

    public static ResourceRecommenderService getResourceRecommenderService(String host, String port) {
        return new ResourceRecommenderService(host, port);
    }
}
