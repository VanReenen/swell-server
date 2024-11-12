package swell.server.model;

import java.math.BigDecimal;

/**
 * Encapsulates the basic data returned for wave and swell data.
 */
public record OpenMateoOceanicWaveDataDTO(String latitude, String longitude, Long utc_offset_seconds,
        String timezone_abbreviation,
        BigDecimal elevation,
        OceanicDataDTO hourly) {

}
