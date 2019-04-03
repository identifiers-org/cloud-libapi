package org.identifiers.cloud.libapi.models.resolver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Project: cloud-libapi
 * Package: org.identifiers.cloud.libapi.models.resolver
 * Timestamp: 2019-04-03 10:10
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Institution implements Serializable {
    // identifiers.org internal ID for this institution
    private long id;
    private String name;
    private String homeUrl;
    private String description;
    // Geographical location for this institution
    private Location location;
}
