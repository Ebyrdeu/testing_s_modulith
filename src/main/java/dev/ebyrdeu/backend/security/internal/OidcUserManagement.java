package dev.ebyrdeu.backend.security.internal;

import dev.ebyrdeu.backend.common.event.OidcUserAuthenticatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
class OidcUserManagement extends OidcUserService {

	private final ApplicationEventPublisher eventPublisher;

	public OidcUserManagement(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public OidcUser loadUser(OidcUserRequest req) throws OAuth2AuthenticationException {
		OidcUser oidcUser = super.loadUser(req);

		eventPublisher.publishEvent(
			new OidcUserAuthenticatedEvent(
				oidcUser.getEmail(),
				oidcUser.getGivenName(),
				oidcUser.getFamilyName(),
				oidcUser.getSubject()
			)
		);

		return oidcUser;
	}
}
