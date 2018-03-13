package org.identifiers.cloud.libapi.models.registry;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: register
 * Package: org.identifiers.org.cloud.ws.register.models
 * Timestamp: 2018-01-30 20:53
 * ---
 *
 * This POJO is part of the payload for requesting a prefix registration or validation operations on the registry
 * service. It models the requester of the prefix registration request.
 */
public class Requester implements Serializable {
    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public Requester setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Requester setEmail(String email) {
        this.email = email;
        return this;
    }
}
