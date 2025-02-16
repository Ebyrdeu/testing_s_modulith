package dev.ebyrdeu.backend.security.internal;

import dev.ebyrdeu.backend.user.UserExternalApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Security filter that ensures the roles of an authenticated user
 * are up-to-date with the roles stored in the database.
 * This filter is executed once per request and refreshes
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see UserExternalApi
 * @see OncePerRequestFilter
 */
@Component
class RoleRefresherFilter extends OncePerRequestFilter {
	private final UserExternalApi userExternalApi;

	public RoleRefresherFilter(UserExternalApi userExternalApi) {
		this.userExternalApi = userExternalApi;
	}

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


		if (authentication == null) {
			filterChain.doFilter(request, response);
			return;
		}

		DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
		String oidcUserEmail = oidcUser.getEmail();
		List<String> oidcUserRoles = oidcUser.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.map((role) -> role.substring(5))
			.toList();


		// NOTE: May not be the most optimal approach to call this method each update request.
		// This is add some stress to backend as each request user will trigger api call
		// it still kinda better than just make user re-log each time I guess.
		// but let's be real - even 100 users won't drop server (at least I hope so)
		List<String> dbUserRoles = this.userExternalApi.findUserRolesByEmail(oidcUserEmail);

		if (oidcUserRoles.equals(dbUserRoles)) {
			filterChain.doFilter(request, response);
			return;
		}

		Collection<GrantedAuthority> updatedRoles = dbUserRoles
			.stream()
			.map(role -> (GrantedAuthority) () -> "ROLE_" + role)
			.collect(Collectors.toSet());

		DefaultOidcUser updatedOidcUser = new DefaultOidcUser(
			updatedRoles,
			oidcUser.getIdToken(),
			oidcUser.getUserInfo(),
			"sub"
		);


		String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

		Authentication newAuthToken = new OAuth2AuthenticationToken(
			updatedOidcUser,
			updatedRoles,
			registrationId
		);


		SecurityContextHolder.getContext().setAuthentication(newAuthToken);

		filterChain.doFilter(request, response);
	}
}
