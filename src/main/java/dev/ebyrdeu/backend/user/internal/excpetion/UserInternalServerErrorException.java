package dev.ebyrdeu.backend.user.internal.excpetion;

public class UserInternalServerErrorException extends RuntimeException {
	public UserInternalServerErrorException(String message) {
		super(message);
	}
}
