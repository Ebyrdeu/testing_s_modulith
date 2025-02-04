package dev.ebyrdeu.backend.user.internal.excpetion;

public class UserServiceException extends RuntimeException {
	public UserServiceException(String message) {
		super(message);
	}
}
