package dev.ebyrdeu.backend.role.internal.exception;

public class RoleNotFoundException extends RuntimeException {
	public RoleNotFoundException(String message) {
		super(message);
	}
}
