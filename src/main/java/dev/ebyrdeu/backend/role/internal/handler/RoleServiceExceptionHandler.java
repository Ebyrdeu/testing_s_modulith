package dev.ebyrdeu.backend.role.internal.handler;

import dev.ebyrdeu.backend.common.dto.ResDto;
import dev.ebyrdeu.backend.role.internal.exception.RoleNotFoundException;
import dev.ebyrdeu.backend.role.internal.exception.RoleServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class RoleServiceExceptionHandler {

	@ExceptionHandler(RoleServiceException.class)
	public ResDto<?> handleRoleServiceException(RoleServiceException exception) {
		return new ResDto<>(
			HttpStatus.INTERNAL_SERVER_ERROR,
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			exception.getMessage(),
			null
		);
	}

	@ExceptionHandler(RoleNotFoundException.class)
	public ResDto<?> handleRoleNotFoundException(RoleNotFoundException exception) {
		return new ResDto<>(
			HttpStatus.NOT_FOUND,
			HttpStatus.NOT_FOUND.value(),
			exception.getMessage(),
			null
		);
	}
}
