package dev.ebyrdeu.backend.user.internal.excpetion;


/**
 * @author Maxim Khnykin
 * @version 1.0
 */
public class UserInternalServerErrorException extends RuntimeException {
	public UserInternalServerErrorException(String message) {
		super(message);
	}
}
