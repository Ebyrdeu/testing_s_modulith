package dev.ebyrdeu.backend.common.util;


import org.springframework.lang.NonNull;

/**
 * An adapter interface for JSON conversion operations using Jackson's {@link com.fasterxml.jackson.databind.ObjectMapper}.
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see com.fasterxml.jackson.databind.ObjectMapper
 */
public interface JsonConverterAdapter {
	<T> String valueOf(@NonNull T obj);

	<T> String valueOf(@NonNull T obj, boolean prettyFormat);
}
