package swell.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import swell.server.exception.XMateoClient;
import swell.server.model.LocationDTO;
import swell.server.model.OpenMateoOceanicWaveDataDTO;
import swell.server.utils.SwellServerTestUtils;

public class SurfSwellServiceTest {

    private SwellServerTestUtils swellServerTestUtils = new SwellServerTestUtils();
    private OpenMateoOceanicWaveDataDTO openMateoOceanicWaveDataDTO;
    private SurfSwellService surfSwellService;
    private LocationDTO testLocation;

    @BeforeEach
    void setup() {
        surfSwellService = mock(SurfSwellService.class, Mockito.CALLS_REAL_METHODS);
        testLocation = swellServerTestUtils.getLocationDTO().build();
        openMateoOceanicWaveDataDTO = swellServerTestUtils.getOpenMateoOceanicWaveDataDTO()
                .hourly(swellServerTestUtils.getOceanicDataDTO().build()).build();

        doReturn(openMateoOceanicWaveDataDTO).when(surfSwellService).getSurfData(anyString(), anyString(),
                anyString());
    }

    @Nested
    class GetWaveHeightData {

        /**
         * Tests that no exception will be thrown, and that the data is correclty sent
         * back.
         */
        @Test
        void waveHeightsPresent() {
            assertEquals(surfSwellService.getWaveHeightData(testLocation),
                    openMateoOceanicWaveDataDTO.hourly().wave_height());
        }

        /**
         * Tests that an {@link XMateoClient} exception is thrown when the response from
         * OpenMateo is empty.
         */
        @Test
        void errorRetrievingData() {
            assertThrows(XMateoClient.class, () -> surfSwellService
                    .getWaveHeightData(LocationDTO.builder().build()));
        }
    }

    @Nested
    class GetWavePeriodData {

        /**
         * Tests that no exception will be thrown, and that the data is correclty sent
         * back.
         */
        @Test
        void wavePeriodsPresent() {
            assertEquals(openMateoOceanicWaveDataDTO.hourly().wave_period(),
                    surfSwellService.getWavePeriodData(testLocation));
        }

        /**
         * Tests that an {@link XMateoClient} exception is thrown when the response from
         * OpenMateo is empty.
         */
        @Test
        void errorRetrievingData() {
            assertThrows(XMateoClient.class, () -> surfSwellService.getWavePeriodData(LocationDTO.builder().build()));
        }
    }

    @Nested
    class GetSwellHeightData {

        /**
         * Tests that no exception will be thrown, and that the data is correclty sent
         * back.
         */
        @Test
        void wavePeriodsPresent() {
            assertEquals(openMateoOceanicWaveDataDTO.hourly().swell_wave_height(),
                    surfSwellService.getSwellHeightData(testLocation));
        }

        /**
         * Tests that an {@link XMateoClient} exception is thrown when the response from
         * OpenMateo is empty.
         */
        @Test
        void errorRetrievingData() {
            assertThrows(XMateoClient.class, () -> surfSwellService.getSwellHeightData(LocationDTO.builder().build()));
        }
    }

    @Nested
    class GetSwellPeriodData {

        /**
         * Tests that no exception will be thrown, and that the data is correclty sent
         * back.
         */
        @Test
        void wavePeriodsPresent() {
            assertEquals(openMateoOceanicWaveDataDTO.hourly().swell_wave_period(),
                    surfSwellService.getSwellPeriodData(testLocation));
        }

        /**
         * Tests that an {@link XMateoClient} exception is thrown when the response from
         * OpenMateo is empty.
         */
        @Test
        void errorRetrievingData() {
            assertThrows(XMateoClient.class, () -> surfSwellService.getSwellPeriodData(LocationDTO.builder().build()));
        }
    }
}
