package dev.ebyrdeu.backend.user.internal.dto;

import java.io.Serializable;
import java.util.List;

public record AuthUserDto(
	String firstName,
	String lastName,
	String username,
	String avatar,
	List<String> roles
) implements Serializable {
}
