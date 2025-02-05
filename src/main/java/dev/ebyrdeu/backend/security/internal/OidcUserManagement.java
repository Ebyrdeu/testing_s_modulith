package dev.ebyrdeu.backend.security.internal;

import dev.ebyrdeu.backend.user.UserExternalApi;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
class OidcUserManagement extends OidcUserService {

	private final ApplicationEventPublisher eventPublisher;
	private final UserExternalApi userExternalApi;

	public OidcUserManagement(ApplicationEventPublisher eventPublisher, UserExternalApi userExternalApi) {
		this.eventPublisher = eventPublisher;
		this.userExternalApi = userExternalApi;
	}

	@Override
	@Transactional
	public OidcUser loadUser(OidcUserRequest req) throws OAuth2AuthenticationException {
		OidcUser oidcUser = super.loadUser(req);


		List<String> retrievedUser = List.of("");

		Collection<GrantedAuthority> roleAuthorities = retrievedUser
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
