package dev.ebyrdeu.backend.common.util;

import java.util.function.Consumer;

/**
 * Interface providing utility methods.
 * <p>
 * Patchable Methods :
 * Defines methods to check if a string is "patchable" — meaning it can be processed after trimming —
 * and conditionally apply a consumer operation on it.
 * </p>
 *
 * <ul>
 *   <li>{@code isStringPatchable(value, consumer)}: applies the consumer if the value is non-null, after trimming.</li>
 *   <li>{@code isStringPatchable(value, consumer, emptyCheck)}: applies the consumer if the value is non-null and,
 *   when {@code emptyCheck} is true, if the trimmed value is not empty.</li>
 * </ul>
 *
 * @author Maxim Khnykin
 * @version 1.0
 */
public interface Utils {

	/**
	 * Check if the provided string is non-null, trim it, and pass it to the given consumer.
	 *
	 * @param value    the input string, may be null
	 * @param consumer the consumer to process the trimmed string
	 */
	void isStringPatchable(String value, Consumer<String> consumer);

	/**
	 * Check if the provided string is non-null, trim it, and if {@code emptyCheck} is true, ensure it is non-empty,
	 * then pass it to the given consumer.
	 *
	 * @param value      the input string, may be null
	 * @param consumer   the consumer to process the trimmed string
	 * @param emptyCheck if true, the trimmed string must be non-empty to be passed to the consumer
	 */
	void isStringPatchable(String value, Consumer<String> consumer, boolean emptyCheck);
}
