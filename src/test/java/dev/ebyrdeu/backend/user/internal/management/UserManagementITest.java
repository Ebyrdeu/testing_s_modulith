package dev.ebyrdeu.backend.user.internal.management;

import dev.ebyrdeu.backend.MockOAuth2Client;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(MockOAuth2Client.class)
class UserManagementITest {

	@Autowired
	private JdbcTemplate jdbcTemplate;


	@BeforeEach
	void setup() {
		String createUser = "INSERT INTO users (id, first_name, last_name, username, email) VALUES (?, ?, ?, ?, ?)";
		this.jdbcTemplate.update(createUser, 1, "John", "Johnson", "JohnJohn", "email@email.com");

		String addAdminRole = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
		this.jdbcTemplate.update(addAdminRole, 1, 3);


	}

}