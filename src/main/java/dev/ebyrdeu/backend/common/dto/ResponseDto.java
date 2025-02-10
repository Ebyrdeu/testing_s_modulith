package dev.ebyrdeu.backend.common.dto;

import org.springframework.http.HttpStatus;

import java.io.Serializable;


/**
 * A generic response structure for API responses, ensuring a consistent format
 * across the application. This class encapsulates the HTTP status, status code,
 * a message, and optional data payload.
 *
 * <p>This record is designed to be used as a standardized response format for
 * all API endpoints, making it easier to handle and parse responses on the client side.</p>
 *
 * @param <T> The type of the data payload included in the response.
 * @author Maxim Khnykin
 * @version 1.0
 * @see HttpStatus
 */
public record ResponseDto<T>(
	HttpStatus status,
	int code,
	String message,
	T data
) implements Serializable {
}
