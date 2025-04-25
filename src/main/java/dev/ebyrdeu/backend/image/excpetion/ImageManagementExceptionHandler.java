package dev.ebyrdeu.backend.image.excpetion;

import dev.ebyrdeu.backend.common.dto.BaseResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Maxim Khnykin
 * @version 1.0
 */
@RestControllerAdvice
class ImageManagementExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ImageInternalServerErrorException.class)
	public BaseResponseDto<?> handleImageInternalServerErrorException(ImageInternalServerErrorException exception) {
		return new BaseResponseDto<>(
			HttpStatus.INTERNAL_SERVER_ERROR,
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			exception.getMessage(),
			null
		);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ImageNotFoundException.class)
	public BaseResponseDto<?> handleImageNotFoundException(ImageNotFoundException exception) {
		return new BaseResponseDto<>(
			HttpStatus.NOT_FOUND,
			HttpStatus.NOT_FOUND.value(),
			exception.getMessage(),
			null
		);
	}
}
