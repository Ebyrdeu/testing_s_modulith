package dev.ebyrdeu.backend.security.internal;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This component handles access denied exceptions by forwarding the request to the root route.
 * <p>
 * The design leverages the frontend tanstack router, where the "/" route is configured to display
 * a 404 page. This approach provides a graceful handling of access denied scenarios without exposing
 * sensitive error details to the client.
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see AccessDeniedException
 * @see org.springframework.security.web.access.AccessDeniedHandlerImpl
 */
@Component
class AccessDeniedHandlerImpl implements AccessDeniedHandler {
	private static final Logger log = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.debug("[AccessDeniedHandlerImpl/handle]:: forwarding to 404 page");
		// TODO: if this is become a problem implement differently
		// NOTE: because of how tanstack router works with react and spring
		// NOTE: / route will redirect 404 page which is desired behavior
		request.getRequestDispatcher("/").forward(request, response);
	}
}
