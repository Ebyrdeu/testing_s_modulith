package dev.ebyrdeu.backend.common.util.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ebyrdeu.backend.common.util.JsonConverterAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link JsonConverterAdapter} interface, providing functionality
 * to convert Java objects to JSON strings using Jackson's {@link ObjectMapper}.
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see JsonConverterAdapter
 * @see ObjectMapper
 * @see JsonProcessingException
 */
@Component
class JasonConverter implements JsonConverterAdapter {
	private static final Logger log = LoggerFactory.getLogger(JasonConverter.class);
	private final ObjectMapper objectMapper;

	public JasonConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * Converts a Java object to its JSON string representation.
	 *
	 * @param <T> The object to convert to JSON.
	 * @return The JSON string representation of the object.
	 * @throws RuntimeException If JSON conversion fails.
	 */
	@NonNull
	@Override
	public <T> String valueOf(@NonNull T obj) {
		try {
			return this.objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error("[JsonConverter/valueOf] - Failed to convert object {} to JSON", obj);
			throw new RuntimeException("Failed to convert object to JSON: " + e.getMessage(), e);
		}
	}

	/**
	 * Converts a Java object to its JSON string representation.
	 * Optionally you can turn on pretty format
	 *
	 * @param <T>          The object to convert to JSON.
	 * @param prettyFormat The boolean that make JSON output formated
	 * @return The JSON string representation of the object.
	 * @throws RuntimeException If JSON conversion fails.
	 */
	@Override
	public <T> String valueOf(@NonNull T obj, boolean prettyFormat) {
		try {
			if (prettyFormat) {
				return this.objectMapper
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(obj);
			}
			return this.objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error("[JsonConverter/valueOf] - Failed to convert object {} to JSON", obj);
			throw new RuntimeException("Failed to convert object to JSON: " + e.getMessage(), e);
		}
	}
}
