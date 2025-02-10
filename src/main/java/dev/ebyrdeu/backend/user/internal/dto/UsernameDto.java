package dev.ebyrdeu.backend.user.internal.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * Request type DTO which allow user to patch username
 *
 * @author Maxim Khnykin
 * @version 1.0
 */
public record UsernameDto(
	@NotNull
	@Size(max = 30)
	String username
) implements Serializable {
}
