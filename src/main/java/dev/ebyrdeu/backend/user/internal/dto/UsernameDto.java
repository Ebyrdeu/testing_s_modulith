package dev.ebyrdeu.backend.user.internal.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for updating the username of a user.
 * <p>
 * This record is used as a request payload when patching a user's username. The username field
 * must not be null and its length should not exceed 30 characters.
 * </p>
 *
 * @param username the new username for the user.
 * @author Maxim Khnykin
 * @version 1.0
 * @see Serializable
 */
public record UsernameDto(
	@NotNull
	@Size(max = 30)
	String username
) implements Serializable {
}
