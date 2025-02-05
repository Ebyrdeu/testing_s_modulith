package dev.ebyrdeu.backend.common.dto;

import org.jmolecules.event.types.DomainEvent;

/**
 * @author Maxim Khnykin
 * @version 1.0
 */
public record OidcUserEventDto(
	String email,
	String firstName,
	String lastName,
	String subject
) implements DomainEvent {
}
