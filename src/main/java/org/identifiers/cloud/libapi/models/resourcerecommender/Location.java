package org.identifiers.cloud.libapi.models.resourcerecommender;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Project: idorg-resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.api.data.models
 * Timestamp: 2019-08-06 10:25
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
public class Location implements Serializable {
    private String countryCode;
    private String countryName;
}