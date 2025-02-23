package dev.ebyrdeu.backend;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@TestConfiguration
class DefaultPostgresContainer {

	@Container
	@ServiceConnection
	private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:17");

}
