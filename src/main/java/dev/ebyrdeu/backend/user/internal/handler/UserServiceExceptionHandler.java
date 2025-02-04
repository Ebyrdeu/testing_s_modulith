package dev.ebyrdeu.backend.user.internal.handler;

import dev.ebyrdeu.backend.common.dto.ResDto;
import dev.ebyrdeu.backend.user.internal.excpetion.UserNotFoundException;
import dev.ebyrdeu.backend.user.internal.excpetion.UserServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
class UserServiceExceptionHandler {

	@ExceptionHandler(UserServiceException.class)
	public ResDto<?> handleUserServiceException(UserServiceException exception) {
		return new ResDto<>(
			HttpStatus.INTERNAL_SERVER_ERROR,
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			exception.getMessage(),
			null
		);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResDto<?> handleUserNotFoundException(UserNotFoundException exception) {
		return new ResDto<>(
			HttpStatus.NOT_FOUND,
			HttpStatus.NOT_FOUND.value(),
			exception.getMessage(),
			null
		);
	}
}
