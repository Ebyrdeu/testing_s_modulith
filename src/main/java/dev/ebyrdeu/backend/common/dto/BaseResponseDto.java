package dev.ebyrdeu.backend.common.dto;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Generic API response wrapper for JSON-based endpoints.
 * <p>
 * Ensures a consistent response format by encapsulating:
 * <ul>
 *   <li>HTTP status ({@link HttpStatus})</li>
 *   <li>Numeric status code (typically matching {@code status.value()})</li>
 *   <li>Human-readable message describing the outcome</li>
 *   <li>Typed data payload</li>
 * </ul>
 * </p>
 * <p>
 * The type parameter {@code T} allows any DTO or primitive type to be returned
 * in the {@code data} field. Spring and Jackson will handle serialization
 * of {@code data} into the JSON response body.
 * </p>
 * <pre>
 * Example serialized output:
 * {
 *   "status": "OK",
 *   "code": 200,
 *   "message": "Request processed successfully",
 *   "data": {
 *     "id": 42,
 *     "name": "Alice"
 *   }
 * }
 * </pre>
 *
 * @param <T>     the type of the response payload
 * @param status  the HTTP status of the response
 * @param code    the numeric HTTP status code
 * @param message a descriptive message for the client
 * @param data    the payload of type {@code T}
 * @author Maxim Khnykin
 * @version 1.0
 * @see BaseResponseJsonDto for raw-JSON payload scenarios
 */
public record BaseResponseDto<T>(
		HttpStatus status,
		int code,
		String message,
		T data
) implements Serializable {
}
