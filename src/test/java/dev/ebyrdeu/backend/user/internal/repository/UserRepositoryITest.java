package dev.ebyrdeu.backend.user.internal.repository;

import dev.ebyrdeu.backend.TestWithPostgresContainer;
import dev.ebyrdeu.backend.user.internal.model.User;
import dev.ebyrdeu.backend.user.internal.projection.UserMinimalInfoProjection;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestWithPostgresContainer
class UserRepositoryITest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private UserRepository userRepository;


	@Test
	@DisplayName("Should Return user successfully when valid email is provided")
	void should_ReturnUserSuccessfully_whenValidEmailIsProvided() {
		// Given
		User user = new User();
		user.setUsername("John");
		user.setEmail("email@email.com");
		this.entityManager.persist(user);
		this.entityManager.flush();

		// When
		Optional<User> res = this.userRepository.findOneByEmail("email@email.com");

		// Then
		assertThat(res).isPresent();
		assertEquals("email@email.com", res.get().getEmail());
	}

	@Test
	@DisplayName("Should Return all users with UserMinimalInfo successfully when repository returns data")
	void should_ReturnAllUsersWithUserMinimalInfo_whenRepositoryReturnsData() {

		// Given
		User userOne = new User();
		userOne.setUsername("one");
		userOne.setFirstName("first_one");
		userOne.setLastName("last_one");
		this.entityManager.persist(userOne);
		this.entityManager.flush();

		User userTwo = new User();
		userTwo.setUsername("two");
		userTwo.setFirstName("first_two");
		userTwo.setLastName("last_two");
		this.entityManager.persist(userTwo);
		this.entityManager.flush();

		// When
		List<UserMinimalInfoProjection> res = this.userRepository.findAllWithMinimalInfo();

		// Then
		assertAll(
			() -> assertFalse(res.isEmpty()),
			() -> assertEquals(2, res.size()),
			() -> assertEquals("first_one", res.getFirst().getFirstName()),
			() -> assertEquals("one", res.getFirst().getUsername()),
			() -> assertEquals("last_one", res.getFirst().getLastName()),
			() -> assertEquals("first_two", res.getLast().getFirstName()),
			() -> assertEquals("two", res.getLast().getUsername()),
			() -> assertEquals("last_two", res.getLast().getLastName())
		);
	}

	@Test
	@DisplayName("Should Return user with UserMinimalInfo successfully when a valid user ID is provided")
	void should_ReturnUserWithUserMinimalInfoSuccessfully_whenAValidUserIdIsProvided() {
		// Given
		User user = new User();
		user.setUsername("user");
		user.setFirstName("first_user");
		user.setLastName("last_user");
		this.entityManager.persist(user);
		this.entityManager.flush();

		// When
		Optional<UserMinimalInfoProjection> res = this.userRepository.findOneByIdWithMinimalInfo(user.getId());
		assertThat(res).isPresent();

		assertAll(
			() -> assertNotNull(res),
			() -> assertEquals("user", res.get().getUsername()),
			() -> assertEquals("first_user", res.get().getFirstName()),
			() -> assertEquals("last_user", res.get().getLastName())
		);
	}

	@Test
	@DisplayName("Should Return user of null when an invalid ID is provided")
	void should_returnUserOfNull_whenAnInvalidIdIsProvided() {
		// When
		Optional<UserMinimalInfoProjection> res = this.userRepository.findOneByIdWithMinimalInfo(1L);


		// Then
		assertTrue(res.isEmpty());
	}


	@Test
	@DisplayName("Should Return User when a valid Email is provided")
	void should_returnUser_whenAValidEmailIsProvided() {
		// Given
		User user = new User();
		user.setUsername("John");
		user.setEmail("email@email.com");
		this.entityManager.persist(user);
		this.entityManager.flush();

		this.jdbcTemplate.update(
			"insert into user_role (user_id, role_id) values (?, ?)",
			user.getId(),
			3L
		);

		// When
		List<String> res = this.userRepository.findRolesByEmail(user.getEmail());


		// Then

		assertAll(
			() -> assertEquals(1, res.size()),
			() -> assertEquals("ADMIN", res.getFirst())
		);
	}

	@Test
	@DisplayName("Should assign given role to user when valid data is provided")
	void should_assignGivenRoleToUser_whenValidDataIsProvided() {
		// Given
		User user = new User();
		user.setUsername("John");
		this.entityManager.persist(user);
		this.entityManager.flush();

		// When
		this.userRepository.addSingleRole(user.getId(), 1L);
		this.userRepository.addSingleRole(user.getId(), 2L);

		// Then
		Long count = this.jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM user_role WHERE user_id = ? ",
			Long.class,
			user.getId()
		);

		assertEquals(2, count);

	}
}