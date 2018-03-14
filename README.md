# Overview
This is a Java library that implements clients for [identifiers.org](https://identifiers.org) Web Services.

The following sections will explain how to use the different service wrappers to access 
[identifiers.org](https://identifiers.org) Web Services on any of its cloud deployments.

## Compact ID Resolution Service
The Following code snippet shows how to get an instance of the service wrapper for 
[identifiers.org](https://identifiers.org) Resolver, and query the service for a given Compact ID.
```java
// For accessing a locally deployed Resolver service at 'localhost:8080', and requesting resolution of 
// Compact ID 'CHEBI:36927' 
ServiceResponseResolve response = ApiServicesFactory
                .getResolverService("localhost", "8080")
                .requestCompactIdResolution("CHEBI:36927");
  
// For accessing a locally deployed Resolver service at 'localhost:8080', and requesting resolution of 
// Compact ID 'CHEBI:36927', but constraining the resource provider to 'ebi' 
ServiceResponseResolve response = ApiServicesFactory
                .getResolverService("localhost", "8080")
                .requestCompactIdResolution("CHEBI:36927", "ebi");
```

Additional constructor are available where only the service 'host' is specified, in that case port '80' will be used, or
where no 'host' or 'port' information is given, so the factory will provide an instance of the service client pointing
to any of the [identifiers.org](https://identifiers.org) cloud deployments.