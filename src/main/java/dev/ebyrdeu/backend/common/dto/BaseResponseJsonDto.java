package dev.ebyrdeu.backend.common.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Generic JSON-based API response wrapper.
 * <p>
 * Encapsulates an HTTP status, status code, message, and a raw JSON payload.
 * The {@code data} field is annotated with {@link JsonRawValue} so that Jackson
 * writes its content directly into the output without additional quoting or escaping.
 * </p>
 * <p>
 * Example serialized output:
 * <pre>
 * {
 *   "status": "OK",
 *   "code": 200,
 *   "message": "Operation successful",
 *   "data": { "key": "value", "list": [1,2,3] }
 * }
 * </pre>
 *
 * @param status  the HTTP status of the response
 * @param code    the numeric status code (usually matches {@code status.value()})
 * @param message a human-readable message describing the result
 * @param data    a raw JSON string payload, inlined into the response body
 * @author Maxim Khnykin
 * @version 1.0
 */
public record BaseResponseJsonDto(
		HttpStatus status,
		int code,
		String message,

		@JsonRawValue
		String data
) implements Serializable {
}
