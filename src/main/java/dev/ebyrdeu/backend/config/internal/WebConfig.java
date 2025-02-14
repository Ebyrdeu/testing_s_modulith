package dev.ebyrdeu.backend.config.internal;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Configuration class to ensure that a React SPA is served correctly
 * by Spring Boot, especially when using client-side routing.
 *
 * <p>This configuration forwards all non-file-extension paths to {@code index.html}, allowing
 * the React application to handle client-side routing without interference from the server.
 * This is necessary because, in an SPA, the React router manages navigation, and the server
 * should only serve the main {@code index.html} file for all routes.</p>
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see WebMvcConfigurer
 * @see ViewControllerRegistry
 * @see CorsRegistry
 */
@Configuration
class WebConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// Forward all sub-paths to index.html for each sector defined in registry
		// NOTE: This can likely be done programmatically for deeper paths if needed.
		// And no... fori loops - is not an option
		registry
			.addViewController("/{s1:[^\\.]*}")
			.setViewName("forward:/index.html");

		registry
			.addViewController("/{s1:[^\\.]*}/{s2:[^\\.]*}")
			.setViewName("forward:/index.html");

		registry
			.addViewController("/{s1:[^\\.]*}/{s2:[^\\.]*}/{s3:[^\\\\.]*}")
			.setViewName("forward:/index.html");
	}

}
