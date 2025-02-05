package dev.ebyrdeu.backend.common.event;

import org.jmolecules.event.types.DomainEvent;

public record OidcUserAuthenticatedEvent(
	String email,
	String firstName,
	String lastName,
	String subject
) implements DomainEvent {
}
