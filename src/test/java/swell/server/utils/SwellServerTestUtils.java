package swell.server.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomStringUtils;

import swell.server.model.LocationDTO;
import swell.server.model.OceanicDataDTO;
import swell.server.model.OpenMateoOceanicWaveDataDTO;

/**
 * Utility methods used for testing.
 */
public class SwellServerTestUtils {

    private Random randomNumberGenerator = new Random();
    private RandomStringUtils randomStringUtils = RandomStringUtils.secureStrong();

    /**
     * @return {@link OpenMateoOceanicWaveDataDTO} populated with random values.
     */
    public OpenMateoOceanicWaveDataDTO.OpenMateoOceanicWaveDataDTOBuilder getOpenMateoOceanicWaveDataDTO() {
        return OpenMateoOceanicWaveDataDTO.builder().elevation(BigDecimal.valueOf(randomNumberGenerator.nextLong()))
                .latitude(randomStringUtils.nextNumeric(8)).longitude(randomStringUtils.nextNumeric(8))
                .timezone_abbreviation("UTC");
    }

    /**
     * @return {@link OceanicDataDTO} populated with random values.
     */
    public OceanicDataDTO.OceanicDataDTOBuilder getOceanicDataDTO() {
        return OceanicDataDTO.builder().projectedWaveFace(getRandomBigDecimals())
                .swell_wave_height(getRandomBigDecimals()).swell_wave_period(getRandomBigDecimals())
                .wave_height(getRandomBigDecimals()).wave_period(getRandomBigDecimals()).time(getLocalDateTimes());
    }

    /**
     * @return a list of random {@link BigDecimal} values.
     */
    public List<BigDecimal> getRandomBigDecimals() {
        var bigDecimalList = new ArrayList<BigDecimal>();

        IntStream.range(1, 21).forEach(i -> {
            bigDecimalList.add(BigDecimal.valueOf(randomNumberGenerator.nextLong()));
        });

        return bigDecimalList;
    }

    /**
     * @return a {@link List} of incrementing {@link LocalDateTime} stamps.
     */
    public List<LocalDateTime> getLocalDateTimes() {
        var dateList = new ArrayList<LocalDateTime>();
        var now = LocalDateTime.now();

        IntStream.range(1, 21).forEach(i -> {
            dateList.add(now.plusHours(3 * i));
        });

        return dateList;
    }

    /**
     * @return {@link LocationDTO} with populated with random values.
     */
    public LocationDTO.LocationDTOBuilder getLocationDTO() {
        return LocationDTO.builder().latitude(randomStringUtils.nextNumeric(8))
                .longitude(randomStringUtils.nextNumeric(8));
    }
}
