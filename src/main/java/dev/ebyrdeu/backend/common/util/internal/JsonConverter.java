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
class JsonConverter implements JsonConverterAdapter {
	private static final Logger log = LoggerFactory.getLogger(JsonConverter.class);
	private final ObjectMapper objectMapper;

	public JsonConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public <T> String valueOf(@NonNull T obj) {
		try {
			return this.objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error("[JsonConverter/valueOf/default] - Failed to convert object {} to JSON", obj);
			throw new RuntimeException("Failed to convert object to JSON: " + e.getMessage(), e);
		}
	}

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
			log.error("[JsonConverter/valueOf/pretty] - Failed to convert object {} to JSON", obj);
			throw new RuntimeException("Failed to convert object to JSON: " + e.getMessage(), e);
		}
	}
}
