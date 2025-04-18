package dev.ebyrdeu.backend;

import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Transactional
@Testcontainers
@DirtiesContext
@ActiveProfiles("test")
@ImportTestcontainers(DefaultPostgresContainer.class)
public @interface TestWithPostgresContainer {
}
