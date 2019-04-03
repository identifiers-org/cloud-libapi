package org.identifiers.cloud.libapi.models.resolver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Project: cloud-libapi
 * Package: org.identifiers.cloud.libapi.models.resolver
 * Timestamp: 2019-04-03 10:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class Location implements Serializable {
    private String countryCode;
    private String countryName;
}
