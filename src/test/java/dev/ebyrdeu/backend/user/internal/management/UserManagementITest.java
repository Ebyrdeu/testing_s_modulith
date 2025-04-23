package dev.ebyrdeu.backend.user.internal.management;

import dev.ebyrdeu.backend.TestOAuth2Client;
import dev.ebyrdeu.backend.TestWithPostgresContainer;
import dev.ebyrdeu.backend.common.dto.BaseResponseDto;
import dev.ebyrdeu.backend.common.dto.BaseResponseJsonDto;
import dev.ebyrdeu.backend.user.UserExternalApi;
import dev.ebyrdeu.backend.user.internal.dto.UsernameDto;
import dev.ebyrdeu.backend.user.internal.excpetion.UserNotFoundException;
import dev.ebyrdeu.backend.user.internal.model.User;
import dev.ebyrdeu.backend.user.internal.projection.UserMinimalInfoProjection;
import dev.ebyrdeu.backend.user.internal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestOAuth2Client
@TestWithPostgresContainer
class UserManagementITest {

	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Autowired
	private UserExternalApi userExternalApi;

	@Autowired
	private UserRepository userRepository;


	@BeforeEach
	void setup() {
		String createUser = "INSERT INTO users (id, first_name, last_name, username, email) VALUES (?, ?, ?, ?, ?)";
		this.jdbcTemplate.update(createUser, 1, "John", "Johnson", "JohnJohn", "email@email.com");

		String addAdminRole = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
		this.jdbcTemplate.update(addAdminRole, 1, 3);

		String updateSequence = "SELECT setval('users_id_seq', (SELECT MAX(id) FROM users))";
		this.jdbcTemplate.execute(updateSequence);
	}


	@Nested
	class FindAll {
		@Test
		@DisplayName("Should return all users successfully when service returns data")
		void should_ReturnAllUsersSuccessfully_whenServiceReturnsData() {
			// Given
			String message = "Users retrieved successfully";
			// When
			BaseResponseDto<List<UserMinimalInfoProjection>> response = userExternalApi.findAll();

			// Then
			assertAll(
				() -> assertNotNull(response),
				() -> assertEquals(HttpStatus.OK, response.status()),
				() -> assertEquals(HttpStatus.OK.value(), response.code()),
				() -> assertEquals(message, response.message()),
				() -> assertEquals(1, response.data().size())
			);
		}
	}

	@Nested
	class FindOne {
		@Test
		@DisplayName("Should return user successfully when a valid user ID is provided")
		void should_ReturnUserSuccessfully_whenAValidUserIdIsProvided() {
			// Given
			String message = "User retrieved successfully";

			// When
			BaseResponseJsonDto response = userExternalApi.findOneByUsername("JohnJohn");

			// Then
			assertAll(
				() -> assertNotNull(response),
				() -> assertEquals(HttpStatus.OK, response.status()),
				() -> assertEquals(HttpStatus.OK.value(), response.code()),
				() -> assertEquals(message, response.message())
			);
		}

		@Test
		@DisplayName("Should throw UserNotFoundException when an invalid user Id is provided")
		void should_ThrowUserNotFoundException_whenAnInvalidUserIdIsProvided() {
			// Given
			String errorMessage = "User with ID JohnJohn100 not found";

			// When
			UserNotFoundException exception = assertThrowsExactly(
				UserNotFoundException.class,
				() -> userExternalApi.findOneByUsername("JohnJohn100")
			);

			// Then
			assertEquals(errorMessage, exception.getMessage());
		}

	}

	@Nested
	class Patch {
		@Test
		@DisplayName("Should update an entity and return BaseResponseDto when valid data is provided")
		void should_UpdateAnEntityAndReturnResponseDto_whenValidDataIsProvided() {
			// Given
			String message = "User patched successfully";
			UsernameDto dto = new UsernameDto(
				"changed username"
			);

			// When
			BaseResponseDto<UsernameDto> response = userExternalApi.patchUsername(1L, dto);

			// Then
			assertAll(
				() -> assertNotNull(response),
				() -> assertEquals(HttpStatus.OK, response.status()),
				() -> assertEquals(HttpStatus.OK.value(), response.code()),
				() -> assertEquals(message, response.message()),
				() -> assertEquals("changed username", response.data().username())
			);
		}

		@Test
		@DisplayName("Should throw UserNotFoundException when an invalid user Id is provided")
		void should_ThrowUserNotFoundException_whenAnInvalidUserIdIsProvided() {
			// Given
			String errorMessage = "User with ID " + 999 + " not found";
			UsernameDto dto = new UsernameDto(
				"changed  username"
			);

			// When
			UserNotFoundException exception = assertThrowsExactly(
				UserNotFoundException.class,
				() -> userExternalApi.patchUsername(999L, dto)
			);

			// Then
			assertEquals(errorMessage, exception.getMessage());
		}

	}

	@Nested
	class Special {


		@Test
		@DisplayName("Should return user roles when a valid Email is provided")
		void should_returnUSerRoles_whenAValidEmailIsProvided() {
			// Given
			String email = "email@email.com";

			// When
			List<String> res = userExternalApi.findUserRolesByEmail(email);

			// Then

			assertAll(
				() -> assertEquals(1, res.size()),
				() -> assertEquals("ADMIN", res.getFirst())
			);

		}


		@Test
		@DisplayName("Should assign role to new user when email doesnt exist")
		void should_assignRoleToNewUser_whenEmailDoesntExist() {
			// Given
			String newEmail = "new.email@email.com";
			OidcUser oidcUser = mock(OidcUser.class);
			when(oidcUser.getEmail()).thenReturn(newEmail);
			when(oidcUser.getGivenName()).thenReturn("John");
			when(oidcUser.getFamilyName()).thenReturn("John");
			when(oidcUser.getSubject()).thenReturn("123");

			// When
			userExternalApi.createOrGetOidcUser(oidcUser);

			// Then
			Optional<User> res = userRepository.findOneByEmail(newEmail);
			assertThat(res).isPresent();

			assertAll(
				() -> assertEquals(newEmail, res.get().getEmail()),
				() -> assertEquals("123", res.get().getUsername()),
				() -> assertEquals("John", res.get().getFirstName()),
				() -> assertEquals("John", res.get().getLastName())
			);
		}


	}


}