package com.example.testing;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModularityTest {

	ApplicationModules modules = ApplicationModules.of(Application.class);

	@Test
	void verify() {
		modules.forEach(System.out::println);

		modules.verify();
	}

	@Test
	void createDocs() {
		new Documenter(modules).writeDocumentation();
	}
}
