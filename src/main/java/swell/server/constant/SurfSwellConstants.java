package swell.server.constant;

/**
 * Constants used in the swell-server.
 */
public interface SurfSwellConstants {

    /**
     * Constant paths used for API requests.
     */
    public class Paths {
        public static final String MARINE_OPEN_API_BASE_URL = "https://marine-api.open-meteo.com";
        public static final String MARINE_OPEN_API_SWELL_PATH = "/v1/marine";
        public static final String SURF_DATA = "/surf";

        public static final String MARINE = "/v1/marine";
        public static final String SEARCH_LOCATION = "/v1/search";
    }

    /**
     * API request parameter names.
     */
    public class Parameters {
        public static final String HOURLY = "hourly";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String WAVE_HEIGHT = "wave_height";
        public static final String WAVE_PERIOD = "wave_period";
        public static final String SWELL_WAVE_HEIGHT = "swell_wave_height";
        public static final String SWELL_WAVE_PERIOD = "swell_wave_period";

        public static final String NAME = "name";
        public static final String LOCATION = "location";
    }
}
