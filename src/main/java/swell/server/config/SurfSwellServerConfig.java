package swell.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import swell.server.constant.SurfSwellConstants;

/**
 * Configuration class for the server.
 */
@Configuration
public class SurfSwellServerConfig {

    public static final String SURF_SWELL_WEB_CLIENT = "surfSwellWebClient";

    @Bean(SURF_SWELL_WEB_CLIENT)
    WebClient surfSwellWebClient() {
        return WebClient.builder().baseUrl(SurfSwellConstants.Paths.MARINE_OPEN_API_BASE_URL).build();
    }
}
