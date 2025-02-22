package dev.ebyrdeu.backend.user.internal.dto;

import jakarta.annotation.Nullable;

import java.io.Serializable;

public record AuthResponseDto(
	@Nullable
	AuthUserDto user,
	boolean status
) implements Serializable {
}
