package org.identifiers.cloud.libapi;

import com.sun.istack.internal.NotNull;
import org.identifiers.cloud.libapi.services.ResourceRecommenderService;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi
 * Timestamp: 2018-03-06 13:18
 * ---
 */
public class ApiServicesFactory {

    public static ResourceRecommenderService getResourceRecommenderService(@NotNull String host) {
        return new ResourceRecommenderService(host, "80");
    }

    public static ResourceRecommenderService getResourceRecommenderService(@NotNull String host, @NotNull String port) {
        return new ResourceRecommenderService(host, port);
    }

    // TODO - Create a method to get a Resource Recommender service pointing at any of our satellite deployments

}
