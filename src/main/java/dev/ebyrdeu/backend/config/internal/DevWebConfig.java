package dev.ebyrdeu.backend.config.internal;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for enabling CORS in a development environment.
 * This configuration is specifically designed for scenarios where the React application is running
 * on a separate development server (Vite) and needs to communicate with
 * the Spring Boot backend.
 *
 * <p>This configuration is only active when the "dev" profile is enabled. It allows requests from
 * the React development server (running on {@code http://localhost:5173}) and supports
 * common HTTP methods.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li><b>CORS Mapping</b>: Enables CORS for all endpoints ({@code /**}).</li>
 *   <li><b>Allowed Origins</b>: Permits requests from {@code http://localhost:5173}.</li>
 *   <li><b>Allowed Methods</b>: Supports {@code GET}, {@code POST}, {@code PUT}, {@code DELETE}, and {@code OPTIONS}.</li>
 *   <li><b>Credentials</b>: Allows credentials (e.g., cookies, authorization headers) to be included in requests.</li>
 *   <li><b>Max Age</b>: Sets the maximum age of the CORS preflight response cache to 3600 seconds (1 hour).</li>
 * </ul>
 *
 * <p>Note: If this configuration becomes cumbersome or needs to be reused across profiles,
 * consider moving it to a more general configuration class (e.g., {@link WebConfig}) and
 * overriding the CORS settings at the bean level.</p>
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see WebMvcConfigurer
 * @see CorsRegistry
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
