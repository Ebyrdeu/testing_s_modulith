package dev.ebyrdeu.backend.config.internal;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Serves the React Single Page Application correctly by forwarding
 * all non-file-extension routes to {@code index.html} for client-side routing.
 * <p>
 * Maps these path patterns to the SPA entrypoint:
 * <ul>
 *   <li>/{s1}</li>
 *   <li>/{s1}/{s2}</li>
 *   <li>/{s1}/{s2}/{s3}</li>
 * </ul>
 * Ensures that React Router can handle deep-links without 404 errors from Spring MVC.
 * </p>
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see WebMvcConfigurer
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
