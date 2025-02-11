package dev.ebyrdeu.backend;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

import static org.mockito.Mockito.mock;


@TestConfiguration
public class MockOAuth2Client {

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		return mock(ClientRegistrationRepository.class);
	}

	@Bean
	public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository() {
		return mock(OAuth2AuthorizedClientRepository.class);
	}
}