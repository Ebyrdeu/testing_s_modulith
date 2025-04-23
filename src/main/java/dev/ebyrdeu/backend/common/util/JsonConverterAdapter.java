package dev.ebyrdeu.backend.common.util;


import org.springframework.lang.NonNull;

/**
 * Adapter interface for converting Java objects to JSON strings.
 * <p>
 * Defines two methods for serialization using Jackson's ObjectMapper:
 * <ul>
 *   <li>{@code valueOf(obj)}: standard JSON serialization.</li>
 *   <li>{@code valueOf(obj, prettyFormat)}: optionally produces pretty-printed JSON.</li>
 * </ul>
 * </p>
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see com.fasterxml.jackson.databind.ObjectMapper
 */
public interface JsonConverterAdapter {
	/**
	 * Serialize an object to its JSON string representation.
	 *
	 * @param <T> the type of the object
	 * @param obj the non-null object to serialize
	 * @return the JSON string
	 * @throws RuntimeException if serialization fails
	 */
	<T> String valueOf(@NonNull T obj);

	/**
	 * Serialize an object to its JSON string representation, with optional pretty-printing.
	 *
	 * @param <T>          the type of the object
	 * @param obj          the non-null object to serialize
	 * @param prettyFormat if {@code true}, use a pretty printer
	 * @return the JSON string
	 * @throws RuntimeException if serialization fails
	 */
	<T> String valueOf(@NonNull T obj, boolean prettyFormat);
}
