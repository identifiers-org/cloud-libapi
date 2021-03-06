package org.identifiers.cloud.libapi.models.resourcerecommender;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.models.ResourceRecommender
 * Timestamp: 2018-03-06 12:01
 * ---
 *
 * This class models the data (payload) that is sent to the Resource Recommender service, when requesting
 * recommendations on one or more resource candidates to provide information on a Compact ID.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRecommendPayload implements Serializable {
    private List<ResolvedResource> resolvedResources;
}
