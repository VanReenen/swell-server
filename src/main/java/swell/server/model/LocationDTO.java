package swell.server.model;

import lombok.Builder;
import lombok.With;

/**
 * Encapsulates the latitude and longitude values of a location.
 */
@Builder
@With
public record LocationDTO(String name, String latitude, String longitude) {

}
