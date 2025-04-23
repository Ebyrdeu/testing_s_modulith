package dev.ebyrdeu.backend.config.internal;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Spring Web configuration for enabling CORS during development.
 * <p>
 * Active only when the 'dev' profile is enabled, allowing the React development
 * server at http://localhost:5173 to make cross-origin requests to this backend.
 * </p>
 * <p>
 * Configured settings:
 * <ul>
 *   <li>Allowed Origins: <code>http://localhost:5173</code></li>
 *   <li>Allowed Methods: GET, POST, PUT, DELETE, OPTIONS</li>
 *   <li>Allow Credentials: true</li>
 *   <li>Max Age: 3600 seconds</li>
 * </ul>
 * </p>
 * <p>
 * When serving the frontend from within Spring Boot in production,
 * this configuration can be removed or merged into a central {@code WebConfig}.
 * </p>
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see WebMvcConfigurer
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
