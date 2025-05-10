package dev.ebyrdeu.backend.user.internal.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO representing the authenticated user's details.
 * <p>
 * This record encapsulates the user's basic information such as first name, last name, username,
 * avatar URL, and a list of roles associated with the user.
 * </p>
 *
 * @param firstName the user's first name.
 * @param lastName  the user's last name.
 * @param username  the unique username of the user.
 * @param avatar    the URL of the user's avatar.
 * @param roles     a list of roles (as strings) associated with the user.
 * @author Maxim Khnykin
 * @version 1.0
 * @see Serializable
 */
public record AuthUserDto(
		String firstName,
		String lastName,
		String username,
		String avatar,
		List<String> roles
) implements Serializable {
}
