package dev.ebyrdeu.backend.common.util.internal;

import dev.ebyrdeu.backend.common.util.Utils;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
class UtilsManagement implements Utils {

	@Override
	public void isStringPatchable(String value, Consumer<String> consumer) {
		if (value != null) {
			String trimmed = value.trim();
			consumer.accept(trimmed);
		}
	}

	@Override
	public void isStringPatchable(String value, Consumer<String> consumer, boolean emptyCheck) {
		if (value != null) {
			String trimmed = value.trim();
			if (!trimmed.isEmpty()) {
				consumer.accept(trimmed);
			}
		}
	}

}
