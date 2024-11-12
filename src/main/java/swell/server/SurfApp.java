package swell.server;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

import swell.server.config.SurfSwellServerConfig;

@SpringBootApplication
@Import(SurfSwellServerConfig.class)
public class SurfApp {

	public static void main(String[] args) {

		new SpringApplicationBuilder().logStartupInfo(true).web(WebApplicationType.SERVLET).sources(SurfApp.class)
				.run(args);

	}
}
