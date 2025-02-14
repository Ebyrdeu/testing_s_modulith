package dev.ebyrdeu.backend.security.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
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
			.loginPage("/login")
			.authorizationEndpoint(auth -> auth
				.baseUri("/login/oauth2/authorization")
			)
			.userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserManagement))
			.defaultSuccessUrl("/", true)
		);

		// Other Logins Config
		// disabled as for now we don't support our own login system
		http.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable);

		// Auth Req Config
		http.authorizeHttpRequests(auth -> auth
			.requestMatchers("/", "/login", "/index.html", "/static/**", "/assets/**", "users/**").permitAll()
			.requestMatchers(request -> request.getRequestURI().startsWith("/api/") &&
				!"document".equalsIgnoreCase(request.getHeader("Sec-Fetch-Dest"))).permitAll()
			.requestMatchers("/api/**").hasRole("ADMIN")
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().authenticated()
		);


		// Custom Filters
		http
			.addFilterBefore(
				roleRefresherFilter,
				OAuth2LoginAuthenticationFilter.class
			);

		return http.build();

	}

}
