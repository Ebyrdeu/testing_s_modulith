package dev.ebyrdeu.backend.user.internal.event;

import dev.ebyrdeu.backend.common.event.OidcUserAuthenticatedEvent;
import dev.ebyrdeu.backend.user.internal.model.User;
import dev.ebyrdeu.backend.user.internal.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
class OidcUserEventListener {
	private final UserRepository userRepository;
	private final ApplicationEventPublisher eventPublisher;

	public OidcUserEventListener(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
		this.userRepository = userRepository;
		this.eventPublisher = eventPublisher;
	}

	@ApplicationModuleListener
	public void on(OidcUserAuthenticatedEvent event) {

		User user = new User();
		user.setEmail(event.email());
		user.setFirstName(event.firstName());
		user.setLastName(event.lastName());
		user.setUsername(event.subject());// subject is placeholder for username

		this.userRepository.save(user);
	}
}
