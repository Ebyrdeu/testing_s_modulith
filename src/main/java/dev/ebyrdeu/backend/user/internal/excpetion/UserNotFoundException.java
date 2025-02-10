package dev.ebyrdeu.backend.user.internal.excpetion;


/**
 * @author Maxim Khnykin
 * @version 1.0
 */
public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) {
		super(message);
	}
}
