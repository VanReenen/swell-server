package swell.server.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.With;

/**
 * Encapsulates the basic data returned for wave and swell data.
 */
@Builder
@With
public record OpenMateoOceanicWaveDataDTO(String latitude, String longitude, Long utc_offset_seconds,
                String timezone_abbreviation,
                BigDecimal elevation,
                OceanicDataDTO hourly) {

}
