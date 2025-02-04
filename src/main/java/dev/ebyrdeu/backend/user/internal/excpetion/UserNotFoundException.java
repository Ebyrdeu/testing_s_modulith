package dev.ebyrdeu.backend.user.internal.excpetion;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) {
		super(message);
	}
}
