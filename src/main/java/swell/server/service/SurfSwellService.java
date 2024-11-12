package swell.server.service;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
         */
        public List<BigDecimal> getWaveHeightData(LocationDTO locationDTO) {
                return getSurfData(locationDTO.latitude(),
                                locationDTO.longitude(), SurfSwellConstants.Parameters.WAVE_HEIGHT).hourly()
                                .wave_height();
        }

        /**
         * @return the wave period of the {@link LocationDTO} provided.
         */
        public List<BigDecimal> getWavePeriodData(LocationDTO locationDTO) {
                return getSurfData(locationDTO.latitude(),
                                locationDTO.longitude(), SurfSwellConstants.Parameters.WAVE_PERIOD).hourly()
                                .wave_period();
        }

        /**
         * @return the swell wave height of the {@link LocationDTO} provided.
         */
        public List<BigDecimal> getSwellHeightData(LocationDTO locationDTO) {
                return getSurfData(locationDTO.latitude(),
                                locationDTO.longitude(), SurfSwellConstants.Parameters.SWELL_WAVE_HEIGHT).hourly()
                                .swell_wave_height();
        }

        /**
         * @return the swell wave period of the {@link LocationDTO} provided.
         */
        public List<BigDecimal> getSwellPeriodData(LocationDTO locationDTO) {
                return getSurfData(locationDTO.latitude(),
                                locationDTO.longitude(), SurfSwellConstants.Parameters.SWELL_WAVE_PERIOD).hourly()
                                .swell_wave_period();
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
        private OpenMateoOceanicWaveDataDTO getSurfData(String latitude, String longitude, String requestData) {
                return webClient.get()
                                .uri(uri -> getSurfDataApiRequest(uri, latitude, longitude, requestData))
                                .accept(MediaType.APPLICATION_JSON).retrieve()
                                .bodyToMono(OpenMateoOceanicWaveDataDTO.class)
                                .blockOptional()
                                .orElseThrow(() -> new XMateoClient("Error occurred while fetching data."));
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
}
