package swell.server.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Encapsulates the data used for wave and swell data.
 */
public record OceanicDataDTO(List<LocalDateTime> time, List<BigDecimal> wave_height, List<BigDecimal> wave_period,
        List<BigDecimal> swell_wave_height, List<BigDecimal> swell_wave_period,
        List<BigDecimal> projectedWaveFace) {

}
