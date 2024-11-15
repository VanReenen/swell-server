package swell.server.service;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import swell.server.config.SurfSwellServerConfig;
import swell.server.constant.SurfSwellConstants;
import swell.server.exception.XMateoClient;
import swell.server.model.LocationDTO;
import swell.server.model.OceanicDataDTO;
import swell.server.model.OpenMateoOceanicWaveDataDTO;

/**
 * Service with methods that makes API calls to Open-Mateo.
 * See {@link https://open-meteo.com}
 */
@Service
public class SurfSwellService {

        private WebClient webClient;

        public SurfSwellService(@Qualifier(SurfSwellServerConfig.SURF_SWELL_WEB_CLIENT) WebClient webClient) {
                this.webClient = webClient;
        }

        /**
         * @return the wave height of the {@link LocationDTO} provided.
         * 
         * @throws XMateoClient when an error occurs while fetching the wave height
         *                      data.
         */
        public List<BigDecimal> getWaveHeightData(LocationDTO locationDTO) {
                try {
                        return getSurfData(locationDTO.latitude(),
                                        locationDTO.longitude(), SurfSwellConstants.Parameters.WAVE_HEIGHT).hourly()
                                        .wave_height();
                } catch (Exception e) {
                        throw new XMateoClient(ErrorMessages.getErrorGettingWaveHeightMessage(e));
                }
        }

        /**
         * @return the wave period of the {@link LocationDTO} provided.
         * 
         * @throws XMateoClient when an error occurs while fetching the wave period
         *                      data.
         */
        public List<BigDecimal> getWavePeriodData(LocationDTO locationDTO) {
                try {
                        return Optional.ofNullable(getSurfData(locationDTO.latitude(),
                                        locationDTO.longitude(), SurfSwellConstants.Parameters.WAVE_PERIOD).hourly()
                                        .wave_period()).orElse(List.of());
                } catch (Exception e) {
                        throw new XMateoClient(ErrorMessages.getErrorGettingWavePeriodMessage(e));
                }

        }

        /**
         * @return the swell wave height of the {@link LocationDTO} provided.
         * 
         * @throws XMateoClient when an error occurs while fetching the swell height
         *                      data.
         */
        public List<BigDecimal> getSwellHeightData(LocationDTO locationDTO) {
                try {
                        return Optional.ofNullable(getSurfData(locationDTO.latitude(),
                                        locationDTO.longitude(), SurfSwellConstants.Parameters.SWELL_WAVE_HEIGHT)
                                        .hourly()
                                        .swell_wave_height()).orElse(List.of());
                } catch (Exception e) {
                        throw new XMateoClient(ErrorMessages.getErrorGettingSwellHeightMessage(e));
                }
        }

        /**
         * @return the swell wave period of the {@link LocationDTO} provided.
         * 
         * @throws XMateoClient when an error occurs while fetching the swell period
         *                      data.
         */
        public List<BigDecimal> getSwellPeriodData(LocationDTO locationDTO) {
                try {
                        return Optional.ofNullable(getSurfData(locationDTO.latitude(),
                                        locationDTO.longitude(), SurfSwellConstants.Parameters.SWELL_WAVE_PERIOD)
                                        .hourly()
                                        .swell_wave_period()).orElse(List.of());
                } catch (Exception e) {
                        throw new XMateoClient(ErrorMessages.getErrorGettingSwellPeriodMessage(e));
                }
        }

        /**
         * @return the projected wave face size by subtracting the swellHeight from the
         *         waveHeight.
         */
        public List<BigDecimal> calculateProjectedWaveFace(List<BigDecimal> waveHeight, List<BigDecimal> swellHeight) {
                var projectedWaveFace = new ArrayList<BigDecimal>();

                for (int i = 0; i < waveHeight.size(); i++) {
                        projectedWaveFace.add(waveHeight.get(i).subtract(swellHeight.get(i)));
                }

                return projectedWaveFace;
        }

        /**
         * @return the timestamps for the swell data.
         */
        public List<LocalDateTime> getTimeStamps() {
                return getSurfData("0", "0", SurfSwellConstants.Parameters.WAVE_HEIGHT).hourly().time();
        }

        /**
         * 
         * @param latitude  of the desired location.
         * @param longitude of the desired location.
         * 
         * @return the full {@link OceanicDataDTO} for the desired location.
         */
        public OceanicDataDTO getOceanicData(String latitude, String longitude) {
                var location = new LocationDTO(latitude, longitude);
                var waveHeight = getWaveHeightData(location);
                var swellWaveHeight = getSwellHeightData(location);

                return new OceanicDataDTO(getTimeStamps(), waveHeight,
                                getWavePeriodData(location), swellWaveHeight,
                                getSwellPeriodData(location),
                                calculateProjectedWaveFace(waveHeight, swellWaveHeight));
        }

        /**
         * Makes the API call to Open-Mateo. For more information regarding this API
         * request, see {@link https://open-meteo.com/en/docs/marine-weather-api}
         * 
         * @param latitude    of the desired location.
         * @param longitude   of the desired location.
         * @param requestData used to communicate to Open-Mateo what data we want from
         *                    the request.
         * 
         * @return an {@link OpenMateoDTO} with the desired information.
         */
        protected OpenMateoOceanicWaveDataDTO getSurfData(String latitude, String longitude, String requestData) {
                return webClient.get()
                                .uri(uri -> getSurfDataApiRequest(uri, latitude, longitude, requestData))
                                .accept(MediaType.APPLICATION_JSON).retrieve()
                                .bodyToMono(OpenMateoOceanicWaveDataDTO.class)
                                .blockOptional()
                                .orElseThrow(() -> new XMateoClient(ErrorMessages.OPEN_MATEO_CLIENT_ERROR));
        }

        /**
         * @return {@link URI} of the API request.
         */
        private URI getSurfDataApiRequest(UriBuilder uriBuilder, String latitude, String longitude,
                        String requestData) {
                return URI.create(formatApiRequest(uriBuilder.path(SurfSwellConstants.Paths.MARINE_OPEN_API_SWELL_PATH)
                                .queryParam(SurfSwellConstants.Parameters.LATITUDE,
                                                latitude)
                                .queryParam(SurfSwellConstants.Parameters.LONGITUDE,
                                                longitude)
                                .queryParam(SurfSwellConstants.Parameters.HOURLY,
                                                requestData)
                                .build()));
        }

        /**
         * @return the API request path formatted to fit the Open-Mateo request URL
         *         syntax.
         */
        private String formatApiRequest(URI path) {
                return path.toString().replace("\"", "");
        }

        /**
         * Error messages used in {@link SurfSwellService}.
         */
        public static class ErrorMessages {
                public static final String OPEN_MATEO_CLIENT_ERROR = "Error occurred while fetching Open Mateo data.";
                private static final String ERROR_GETTING_WAVE_HEIGHT = """
                                Error occurred while fetching wave height data. The following exception was thrown:
                                %s
                                """;

                private static final String ERROR_GETTING_WAVE_PERIOD = """
                                Error occurred while fetching wave period data. The following exception was thrown:
                                %s
                                """;

                private static final String ERROR_GETTING_SWELL_HEIGHT = """
                                Error occurred while fetching swell wave height data. The following exception was thrown:
                                %s
                                """;

                private static final String ERROR_GETTING_SWELL_PERIOD = """
                                Error occurred while fetching swell wave period data. The following exception was thrown:
                                %s
                                """;

                public static String getErrorGettingWaveHeightMessage(Exception e) {
                        return ERROR_GETTING_WAVE_HEIGHT.formatted(e);
                }

                public static String getErrorGettingWavePeriodMessage(Exception e) {
                        return ERROR_GETTING_WAVE_PERIOD.formatted(e);
                }

                public static String getErrorGettingSwellHeightMessage(Exception e) {
                        return ERROR_GETTING_SWELL_HEIGHT.formatted(e);
                }

                public static String getErrorGettingSwellPeriodMessage(Exception e) {
                        return ERROR_GETTING_SWELL_PERIOD.formatted(e);
                }
        }
}
