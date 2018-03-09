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

    // --- Resource Recommender Service ---
    public static ResourceRecommenderService getResourceRecommenderService(String host) {
        return new ResourceRecommenderService(host, "80");
    }

    public static ResourceRecommenderService getResourceRecommenderService(String host, String port) {
        return new ResourceRecommenderService(host, port);
    }
    // --- END ---

    // --- Resolver Service ---
    public static ResolverService getResolverService(String host) {
        return getResolverService(host, "80");
    }

    public static ResolverService getResolverService(String host, String port) {
        return new ResolverService(host, port);
    }

    public static ResolverService getResolverService() {
        return getResolverService(Configuration.getServiceLocation(Configuration.ServiceName.RESOLVER));
    }
    // --- END ---

    // --- Metadata Service ---
    public static MetadataService getMetadataService(String host) {
        return getMetadataService(host, "80");
    }

    public static MetadataService getMetadataService(String host, String port) {
        return new MetadataService(host, port);
    }

    public static MetadataService getMetadataService() {
        return getMetadataService(Configuration.getServiceLocation(Configuration.ServiceName.METADATA));
    }
    // --- END ---

    // --- Registry Service ---
    public static RegistryService getRegistryService(String host) {
        return getRegistryService(host, "80");
    }

    public static RegistryService getRegistryService(String host, String port) {
        return new RegistryService(host, port);
    }

    public static RegistryService getRegistryService() {
        return getRegistryService(Configuration.getServiceLocation(Configuration.ServiceName.REGISTRY));
    }
    // --- END ---
}
