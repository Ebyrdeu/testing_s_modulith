package dev.ebyrdeu.backend.user.internal.event;

import dev.ebyrdeu.backend.common.dto.OidcUserEventDto;
import dev.ebyrdeu.backend.user.internal.model.User;
import dev.ebyrdeu.backend.user.internal.repository.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling events related to the creation of users
 * through OIDC authentication. This class listens for {@link OidcUserEventDto}
 * events and processes them to either find an existing user or create a new user in the database.
 *
 * @author Maxim Khnykin
 * @version 1.0
 */
@Service
class OidcUserCreatedEvent {

	private final UserRepository userRepository;

	public OidcUserCreatedEvent(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Listens for {@link OidcUserEventDto} events and processes them.
	 * If a user with the provided email already exists, no action is taken.
	 * If the user does not exist, a new user is created using the details from the event.
	 *
	 * @param event the {@link OidcUserEventDto} containing user details from the OIDC authentication process
	 */
	@EventListener
	private void on(OidcUserEventDto event) {
		this.userRepository
			.findOneByEmail(event.email())
			.orElseGet(() -> this.createUser(event));
	}

	private User createUser(OidcUserEventDto event) {
		User user = new User();
		user.setEmail(event.email());
		user.setFirstName(event.firstName());
		user.setLastName(event.lastName());
		user.setUsername(event.subject());

		User createdUser = this.userRepository.save(user);

		// USER - 1
		// VENDOR - 2
		// ADMIN - 3
		// Unless you want to test staff - stick with 1
		this.userRepository.addSingleRole(createdUser.getId(), 1L);

		return createdUser;
	}
}
