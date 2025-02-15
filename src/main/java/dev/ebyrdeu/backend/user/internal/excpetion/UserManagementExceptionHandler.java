package dev.ebyrdeu.backend.user.internal.excpetion;

import dev.ebyrdeu.backend.common.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Maxim Khnykin
 * @version 1.0
 */
@RestControllerAdvice
class UserManagementExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(UserInternalServerErrorException.class)
	public ResponseDto<?> handleUserServiceException(UserInternalServerErrorException exception) {
		return new ResponseDto<>(
			HttpStatus.INTERNAL_SERVER_ERROR,
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			exception.getMessage(),
			null
		);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseDto<?> handleUserNotFoundException(UserNotFoundException exception) {
		return new ResponseDto<>(
			HttpStatus.NOT_FOUND,
			HttpStatus.NOT_FOUND.value(),
			exception.getMessage(),
			null
		);
	}
}
