package dev.ebyrdeu.backend.security.internal;

import dev.ebyrdeu.backend.user.UserExternalApi;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This Service ensures that the user's roles are synchronized with the database and included in the
 * OIDC user object returned by the `loadUser` method.
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see UserExternalApi
 * @see OidcUserService
 */
@Service
class OidcUserManagement extends OidcUserService {

	private final UserExternalApi userExternalApi;

	public OidcUserManagement(UserExternalApi userExternalApi) {
		this.userExternalApi = userExternalApi;
	}

	@Override
	public OidcUser loadUser(OidcUserRequest req) throws OAuth2AuthenticationException {
		OidcUser oidcUser = super.loadUser(req);

		this.userExternalApi.createOrGetOidcUser(oidcUser);

		List<String> retrievedUserRoles = this.userExternalApi.findUserRolesByEmail(oidcUser.getEmail());

		Collection<GrantedAuthority> roleAuthorities = retrievedUserRoles
				.stream()
				.map(role -> (GrantedAuthority) () -> "ROLE_" + role)
				.collect(Collectors.toSet());

		return new DefaultOidcUser(
				roleAuthorities,
				oidcUser.getIdToken(),
				oidcUser.getUserInfo(),
				"sub"
		);

	}
}
