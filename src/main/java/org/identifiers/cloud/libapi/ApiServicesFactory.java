package org.identifiers.cloud.libapi;

import org.identifiers.cloud.libapi.services.MetadataService;
import org.identifiers.cloud.libapi.services.RegistryService;
import org.identifiers.cloud.libapi.services.ResolverService;
import org.identifiers.cloud.libapi.services.ResourceRecommenderService;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi
 * Timestamp: 2018-03-06 13:18
 * ---
 */
public class ApiServicesFactory {

    public static ResourceRecommenderService getResourceRecommenderService(String host) {
        return new ResourceRecommenderService(host, "80");
    }

    public static ResourceRecommenderService getResourceRecommenderService(String host, String port) {
        return new ResourceRecommenderService(host, port);
    }

    // TODO - Create a method to get a Resource Recommender service pointing at any of our satellite deployments

    public static ResolverService getResolverService(String host) {
        return getResolverService(host, "80");
    }

    public static ResolverService getResolverService(String host, String port) {
        return new ResolverService(host, port);
    }

    // TODO - Create a method to get a Resolver service pointing at any of our satellite deployments

    public static MetadataService getMetadataService(String host) {
        return getMetadataService(host, "80");
    }

    public static MetadataService getMetadataService(String host, String port) {
        return new MetadataService(host, port);
    }

    // TODO - Create a method to get a Metadata service pointing at any of our satellite deployments

    public static RegistryService getRegistryService(String host) {
        return getRegistryService(host, "80");
    }

    public static RegistryService getRegistryService(String host, String port) {
        return new RegistryService(host, port);
    }

    // TODO - Create a method to get a Registry service pointing at any of our satellite deployments
}
