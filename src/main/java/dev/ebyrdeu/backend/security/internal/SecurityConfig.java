package dev.ebyrdeu.backend.security.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;

/**
 * @author Maxim Khnykin
 * @version 1.0
 * @see CsrfTokenRequestHandler
 * @see OidcUserManagement
 * @see RoleRefresherFilter
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {


	@Bean
	public SecurityFilterChain securityFilterChain(
		HttpSecurity http,
		CsrfTokenRequestHandler csrfTokenRequestHandler,
		AccessDeniedHandler accessDeniedHandler,
		OidcUserManagement oidcUserManagement,
		RoleRefresherFilter roleRefresherFilter
	) throws Exception {

		// Csrf Config
		http.csrf(csrf -> csrf
			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.csrfTokenRequestHandler(csrfTokenRequestHandler)
		);

		// Cors config
		http.cors(Customizer.withDefaults());

		// Oauth Config
		http.oauth2Login(oauth2 -> oauth2
			.loginPage("/auth")
			.authorizationEndpoint(auth -> auth
				.baseUri("/login/oauth2/authorization")
			)
			.userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserManagement))
			.defaultSuccessUrl("/", false)
		);

		// Logout config
		http.logout((logout) -> logout.logoutUrl("/api/v1/logout"));

		// Exception Config
		http.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler));

		// Auth Req Config
		http.authorizeHttpRequests(auth -> auth
			.requestMatchers(
				"/",
				"/auth",
				"/index.html",
				"/vite.svg",
				"/static/**",
				"/assets/**",
				"users/**"
			).permitAll()
			.requestMatchers(request -> {
				boolean requestedUri = request.getRequestURI().startsWith("/api/");
				String header = request.getHeader("X-Requested-With");

				// TODO: look at referer
				// NOTE: ideally i don't wont use custom headers
				// NOTE: maybe add this to filter instead
				// allowing users to use our api with fetch on frontend
				// but forbid for them to  api directly
				// or create own fetch request
				// unless they know our header for requests
				return requestedUri && "fetch".equalsIgnoreCase(header);
			}).permitAll()
			.requestMatchers("/api/**").hasRole("ADMIN")
			.anyRequest().authenticated()
		);


		// Custom Filters
		http
			.addFilterBefore(
				roleRefresherFilter,
				OAuth2LoginAuthenticationFilter.class
			);

		// Other Logins Config
		// disabled as for now we don't support our own login system
		http.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable);

		return http.build();

	}

}
