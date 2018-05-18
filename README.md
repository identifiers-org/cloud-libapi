# Overview
This is a Java library that implements clients for [identifiers.org](https://identifiers.org) Web Services.

The following sections will explain how to use the different service wrappers to access
[identifiers.org](https://identifiers.org) Web Services on any of its cloud deployments.


# How to link this library in your code
This library is available at Maven Central, you can use it by just adding the following dependency:

**Maven**
```xml
<dependency>
    <groupId>org.identifiers.cloud</groupId>
    <artifactId>libapi</artifactId>
    <version>1.0.2</version>
</dependency>
```

**Apache Buildr**
```
'org.identifiers.cloud:libapi:jar:1.0.2'
```

**Apache Ivy**
```xml
<dependency org="org.identifiers.cloud" name="libapi" rev="1.0.2" />
```

**Groovy Grape**
```groovy
@Grapes(
@Grab(group='org.identifiers.cloud', module='libapi', version='1.0.2')
)
```

**Gradle/Grails**
```gradle
compile 'org.identifiers.cloud:libapi:1.0.2'
```

**Scala SBT**
```scala
libraryDependencies += "org.identifiers.cloud" % "libapi" % "1.0.2"
```

**Leiningen**
```
[org.identifiers.cloud/libapi "1.0.2"]
```

# Using to [identifiers.org](https://identifiers.org) API Web Services
## Compact ID Resolution Service
The following code snippet shows how to get an instance of the service wrapper for
[identifiers.org](https://identifiers.org) _Resolver Service_, and query the service for a given Compact ID.
```java
import org.identifiers.cloud.libapi.services.ApiServicesFactory;
import org.identifiers.cloud.libapi.models.resolver.ServiceResponseResolve;

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

Additional factory methods are available where only the service 'host' is specified, in that case port '80' will be
used, or where no 'host' or 'port' information is given, so the factory will provide an instance of the service client
pointing to any of the [identifiers.org](https://identifiers.org) cloud deployments.


## Metadata Service
The following code snippet shows how to get an instance of the service wrapper for
[identifiers.org](https://identifiers.org) _Metadata Service_, and submit metadata requests.
```java
import org.identifiers.cloud.libapi.services.ApiServicesFactory;
import org.identifiers.cloud.libapi.models.metadata.ServiceResponseFetchMetadata;
import org.identifiers.cloud.libapi.models.metadata.ServiceResponseFetchMetadataForUrl;

// Requesting metadata for a given Compact ID, the Metadata Service will choose the resource provider with the highest
// recommendation index / score.
ServiceResponseFetchMetadata response = ApiServicesFactory
                .getMetadataService("localhost", "8082")
                .getMetadataForCompactId("CHEBI:36927");

// Requesting metadata for a given URL.
ServiceResponseFetchMetadataForUrl response = ApiServicesFactory
                .getMetadataService("localhost", "8082")
                .getMetadataForUrl("http://reactome.org/content/detail/R-HSA-201451");
```

Additional factory methods are available where only the service 'host' is specified, in that case port '80' will be
used, or where no 'host' or 'port' information is given, so the factory will provide an instance of the service client
pointing to any of the [identifiers.org](https://identifiers.org) cloud deployments.


## Registry Service
The following code snippet shows how to get an instance of the service wrapper for
[identifiers.org](https://identifiers.org) _Registry Service_, and submit registration or validation requests.
```java
import org.identifiers.cloud.libapi.models.registry.Requester;
import org.identifiers.cloud.libapi.models.registry.requests.prefixregistration.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.libapi.models.registry.responses.prefixregistration.ServiceResponseRegisterPrefix;
import org.identifiers.cloud.libapi.models.registry.responses.validation.ServiceResponseValidateRequest;


// Preparing a prefix registration request payload
ServiceRequestRegisterPrefixPayload payload =
            new ServiceRequestRegisterPrefixPayload()
                                    .setName("A Name for this prefix Registration Request")
                                    .setDescription("This is a sample prefix registration request from a unit test of libapi, " +
                                            "we need enouch characters for the description")
                                    .setHomePage("http://your_home.page")
                                    .setOrganization("Your Organization")
                                    .setPreferredPrefix("mynewprefix")
                                    .setResourceAccessRule("http://whatever_url/{$id}")
                                    .setExampleIdentifier("a_sample_id")
                                    .setRegexPattern("\\d+")
                                    .setReferences(new String[]{"ref1", "ref2"})
                                    .setAdditionalInformation("Additional information about this unit test")
                                    .setRequester(new Requester()
                                            .setEmail("requester@your_organization.mail")
                                            .setName("Requester Full Name"));

// Prefix registration request
ServiceResponseRegisterPrefix response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .requestPrefixRegistration(payload);

// There are methods for individual validation of the prefix registration payload fields, they all use the same
// 'ServiceRequestRegisterPrefixPayload' payload object, filled only with the field that wants to be validated. As an
// example, the following lines of code will validate the 'name' field.
// Fill in the payload with just the 'name' field
ServiceRequestRegisterPrefixPayload payload =
            new ServiceRequestRegisterPrefixPayload()
                                    .setName("A Name for this prefix Registration Request");
// Request validation
ServiceResponseValidateRequest response =
                ApiServicesFactory.getRegistryService("localhost", "8081")
                        .requestValidationName(payload);
```

Additional factory methods are available where only the service 'host' is specified, in that case port '80' will be
used, or where no 'host' or 'port' information is given, so the factory will provide an instance of the service client
pointing to any of the [identifiers.org](https://identifiers.org) cloud deployments.

## Responses from the services
The responses from the different services will provide information on how the request was completed, via HTTP Status
code and a possible error message, as well as a specialized payload for the particular request submitted.

For further details, please refer to the javadoc accompanying this library.

# Library Configuration
This library is able to provide clients for the different clouds where [identifiers.org](https://identifiers.org) has
deployed its services, i.e. Amazon Web Services, Google Cloud or Microsoft Azure. By default, a deployment is chosen
randomly between all the possible ones every time a web service client is requested, but this behaviour can be modified
for those use cases where we would like to lock in a cloud provider, i.e. we would like to use [identifiers.org](https://identifiers.org)
web services that are part of only one cloud deployment, this can be done using a _deployment selector_ within the
library configuration as shown in the following code snippet.

````java
import org.identifiers.cloud.libapi.Configuration;

// This call we'll make the library always select identifiers.org AWS deployment
Configuration.selectDeployment(Configuration.InfrastructureDeploymentSelector.AWS);

// To make the library select random deployments again (default behaviour), we use the 'ANY' selector
Configuration.selectDeployment(Configuration.InfrastructureDeploymentSelector.ANY);
````


### Contact
Manuel Bernal Llinares
