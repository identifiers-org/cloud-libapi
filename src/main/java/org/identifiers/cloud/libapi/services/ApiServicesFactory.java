package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.libapi.Configuration;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi
 * Timestamp: 2018-03-06 13:18
 * ---
 *
 * Service wrappers / clients are meant to be instantiated via this Abstract Factory.
 *
 * The mechanism for getting an instance of every service client is the same for all of them, and it has three different
 * options:
 *  - With no information: a random deployment for the service from identifiers.org will be used.
 *  - With host information: the client will use the service on that host on the default port 80.
 *  - With host and port information: the client will use the service on the specified host and port.
 *
 * As you may notice, there is no factory method for a random deployment of the Resource Recommender service, as it is
 * used internally in identifiers.org satellite deployments, but it is not offered to the public.
 */
public class ApiServicesFactory {

    // --- Resource Recommender Service ---
    /**
     * Get an instance of the Resource Recommender service client, pointing to the given host, on default port 80.
     * @param host host running the service.
     * @return an instance of the service client pointing to the given host.
     */
    public static ResourceRecommenderService getResourceRecommenderService(String host) {
        return new ResourceRecommenderService(host, "80");
    }

    /**
     * Get an instance of the Resource Recommender service client, pointing to the given host and port.
     * @param host host running the service.
     * @param port port where the service is listening for connections.
     * @return an instance of the service client.
     */
    public static ResourceRecommenderService getResourceRecommenderService(String host, String port) {
        return new ResourceRecommenderService(host, port);
    }
    // --- END ---

    // --- Resolver Service ---
    /**
     * Get an instance of the Resolver service client, pointing to the given host, on default port 80.
     * @param host host running the service
     * @return an instance of the service client pointing to the given host.
     */
    public static ResolverService getResolverService(String host) {
        return getResolverService(host, "80");
    }

    /**
     * Get an instance of the Resolver service client, pointing to the given host and port.
     * @param host host running the service.
     * @param port port where the service is listening for connections.
     * @return an instance of the service client.
     */
    public static ResolverService getResolverService(String host, String port) {
        return new ResolverService(host, port);
    }

    public static ResolverService getResolverService() {
        return getResolverService(Configuration.getServiceLocation(Configuration.ServiceName.RESOLVER));
    }
    // --- END ---

    // --- Metadata Service ---
    /**
     * Get an instance of the Metadata service client, pointing to the given host, on default port 80.
     * @param host host running the service
     * @return an instance of the service client pointing to the given host.
     */
    public static MetadataService getMetadataService(String host) {
        return getMetadataService(host, "80");
    }

    /**
     * Get an instance of the Metadata service client, pointing to the given host and port.
     * @param host host running the service.
     * @param port port where the service is listening for connections.
     * @return an instance of the service client.
     */
    public static MetadataService getMetadataService(String host, String port) {
        return new MetadataService(host, port);
    }

    /**
     * Get an instance of the Metadata service client, randomly pointing to one of the identifiers.org deployments
     * (AWS, Google Cloud, Azure...).
     * @return an instance of the service client
     */
    public static MetadataService getMetadataService() {
        return getMetadataService(Configuration.getServiceLocation(Configuration.ServiceName.METADATA));
    }
    // --- END ---

    // --- Registry Service ---
    /**
     * Get an instance of the Registry service client, pointing to the given host, on default port 80.
     * @param host host running the service
     * @return an instance of the service client pointing to the given host.
     */
    public static RegistryService getRegistryService(String host) {
        return getRegistryService(host, "80");
    }

    /**
     * Get an instance of the Registry service client, pointing to the given host and port.
     * @param host host running the service.
     * @param port port where the service is listening for connections.
     * @return an instance of the service client.
     */
    public static RegistryService getRegistryService(String host, String port) {
        return new RegistryService(host, port);
    }

    /**
     * Get an instance of the Registry service client, randomly pointing to one of the identifiers.org deployments
     * (AWS, Google Cloud, Azure...).
     * @return an instance of the service client.
     */
    public static RegistryService getRegistryService() {
        return getRegistryService(Configuration.getServiceLocation(Configuration.ServiceName.REGISTRY));
    }
    // --- END ---
}
