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
 * <p>Key Components:</p>
 * <ul>
 *   <li><b>status</b>: The HTTP status of the response (e.g., {@link HttpStatus#OK}).</li>
 *   <li><b>code</b>: The numeric HTTP status code (e.g., 200 for OK).</li>
 *   <li><b>message</b>: A human-readable message describing the response.</li>
 *   <li><b>data</b>: The optional payload or data associated with the response.</li>
 * </ul>
 *
 * <p>Usage Example:</p>
 * <pre>
 * {@code
 * WebResDto<String> response = new WebResDto<>(
 *     HttpStatus.OK,
 *     200,
 *     "Operation successful",
 *     "Sample data"
 * );
 * }
 * </pre>
 *
 * @param <T> The type of the data payload included in the response.
 * @author Maxim Khnykin
 * @version 1.0
 */
public record ResDto<T>(
	HttpStatus status,
	int code,
	String message,
	T data
) implements Serializable {
}
