package dev.ebyrdeu.backend;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class DefaultPostgresContainer {

	@Container
	@ServiceConnection
	private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:17");

}
