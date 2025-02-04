package dev.ebyrdeu.backend.user.internal.web.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * @author Maxim Khnykin
 * @version 1.0
 */
public record UsernameReqDto(
	@NotNull
	@Size(max = 30)
	String username
) implements Serializable {
}
