package dev.ebyrdeu.backend.security.internal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;


/**
 * Custom implementation of {@link CsrfTokenRequestHandler} that integrates both plain and XOR-based CSRF token handling.
 * This class is designed to support secure CSRF token validation for JavaScript-based SPAs.
 *
 * <p>This handler combines the functionality of {@link CsrfTokenRequestAttributeHandler} (plain CSRF tokens) and
 * {@link XorCsrfTokenRequestAttributeHandler} (XOR-encoded CSRF tokens) to provide a flexible and secure solution
 * for CSRF protection.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li><b>Dual Token Handling</b>: Supports both plain and XOR-encoded CSRF tokens for compatibility with different client implementations.</li>
 *   <li><b>Secure Token Resolution</b>: Automatically resolves the appropriate token value based on the request headers.</li>
 *   <li><b>Integration with Spring Security</b>: Seamlessly integrates with Spring Security's CSRF protection mechanisms.</li>
 * </ul>
 *
 * <p>For more information on Spring Security CSRF token integration, refer to the
 * <a href="https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa">official documentation</a>.</p>
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see CsrfTokenRequestHandler
 * @see CsrfTokenRequestAttributeHandler
 * @see XorCsrfTokenRequestAttributeHandler
 */
@Service
class WebCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
	private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
	private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
		this.xor.handle(request, response, csrfToken);

		csrfToken.get();
	}

	@Override
	public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
		String headerValue = request.getHeader(csrfToken.getHeaderName());

		return (StringUtils.hasText(headerValue) ? this.plain : this.xor).resolveCsrfTokenValue(request, csrfToken);
	}
}
