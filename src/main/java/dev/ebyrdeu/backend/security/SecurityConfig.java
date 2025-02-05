package dev.ebyrdeu.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;

/**
 * @author Maxim Khnykin
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {


	@Bean
	public SecurityFilterChain securityFilterChain(
		HttpSecurity http,
		CsrfTokenRequestHandler csrfTokenRequestHandler,
		OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService
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
			.userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService))
			.defaultSuccessUrl("/", true)
		);

		// Other Logins Config
		http.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable);

		// Auth Req Config
		http.authorizeHttpRequests(auth -> auth
			.requestMatchers("/", "/login", "/index.html", "/static/**", "/assets/**").permitAll()
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().permitAll()
		);

		return http.build();

	}

}
