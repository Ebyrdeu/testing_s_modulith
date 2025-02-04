package dev.ebyrdeu.backend.common.util;


/**
 * An adapter interface for JSON conversion operations using Jackson's {@link com.fasterxml.jackson.databind.ObjectMapper}.
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see com.fasterxml.jackson.databind.ObjectMapper
 */
public interface JsonConverterAdapter {
	String valueOf(Object obj);
}
