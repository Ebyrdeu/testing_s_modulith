package dev.ebyrdeu.backend.user.internal.dto;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for encapsulating the authentication response.
 * <p>
 * This record holds the details of the authenticated user along with a status flag
 * indicating whether the authentication was successful.
 * </p>
 *
 * @param user   the authenticated user's details; may be {@code null} if no user is authenticated.
 * @param status a boolean flag indicating the authentication status: {@code true} if authenticated, {@code false} otherwise.
 * @author Maxim Khnykin
 * @version 1.0
 * @see AuthUserDto
 */
public record AuthResponseDto(
	@Nullable
	AuthUserDto user,
	boolean status
) implements Serializable {
}
