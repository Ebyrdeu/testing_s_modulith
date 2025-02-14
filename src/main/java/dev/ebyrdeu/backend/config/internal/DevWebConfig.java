package dev.ebyrdeu.backend.config.internal;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * This configuration is necessary only if you are running the React app
 * separately using a development server  rather than
 * serving it from within the Spring Boot application.
 * <p/>
 * NOTE: If become bothersome need to move in {@link WebConfig}
 * but that also requires creating bean level override
 */
@Configuration
@Profile("dev")
class DevWebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:5173")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowCredentials(true)
			.maxAge(3600);
	}
}
