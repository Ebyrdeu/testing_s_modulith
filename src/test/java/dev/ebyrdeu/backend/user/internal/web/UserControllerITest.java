package dev.ebyrdeu.backend.user.internal.web;

import dev.ebyrdeu.backend.TestOAuth2Client;
import dev.ebyrdeu.backend.TestWithPostgresContainer;
import dev.ebyrdeu.backend.common.util.JsonConverterAdapter;
import dev.ebyrdeu.backend.user.internal.dto.UserInfoReqDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestOAuth2Client
@TestWithPostgresContainer
@AutoConfigureMockMvc
class UserControllerITest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JsonConverterAdapter jsonConverterAdapter;

	@BeforeEach
	void setup() {
		String createUser = "INSERT INTO users (id, first_name, last_name, username, email) VALUES (?, ?, ?, ?, ?)";
		this.jdbcTemplate.update(createUser, 1, "John", "Johnson", "JohnJohn", "email@email.com");

		String addAdminRole = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
		this.jdbcTemplate.update(addAdminRole, 1, 3);
	}

	@Nested
	class findAll {

		@Test
		@DisplayName("Should return all Users when the request is successful")
		void shouldReturnAllUsersWhenRequestIsSuccessful() throws Exception {
			// Given
			String expectedStatus = "OK";
			int expectedCode = HttpStatus.OK.value();
			String expectedMessage = "Users retrieved successfully";
			String expectedResponse = """
				{
				"status":"OK",
				"code":200,
				"message":"Users retrieved successfully",
				"data": [{"username":"JohnJohn", "firstName":"John","lastName":"Johnson"}]
				}
				""";


			// When
			ResultActions response = mockMvc
				.perform(
					get("/api/v1/users")
						.with(csrf())
						.with(oidcLogin()
							.idToken(token -> token.claim("email", "email@email.com"))
						)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
				);

			// Then
			response
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value(expectedStatus))
				.andExpect(jsonPath("$.code").value(expectedCode))
				.andExpect(jsonPath("$.message").value(expectedMessage))
				.andExpect(jsonPath("$.data").isArray())
				.andExpect(content().json(expectedResponse))
				.andDo(print());


		}

	}

	@Nested
	class FindOne {

		@Test
		@DisplayName("Should return a single user when a valid ID is provided")
		void shouldReturnSingleUserWhenValidIdProvided() throws Exception {
			// Given
			String expectedStatus = "OK";
			int expectedCode = HttpStatus.OK.value();
			String expectedMessage = "User retrieved successfully";
			String expectedResponse = """
					{
					"status":"OK",
					"code":200,
					"message":"User retrieved successfully",
					"data":{"username" : "JohnJohn", "firstName" : "John", "lastName" : "Johnson", "email" : "email@email.com", "images" : []}	
					}
				""";

			// When

			ResultActions response = mockMvc
				.perform(
					get("/api/v1/users/JohnJohn")
						.with(csrf())
						.with(oidcLogin().idToken(token -> token.claim("email", "email@email.com")))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
				);


			// Then
			response
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value(expectedStatus))
				.andExpect(jsonPath("$.code").value(expectedCode))
				.andExpect(jsonPath("$.message").value(expectedMessage))
				.andExpect(content().json(expectedResponse))
				.andDo(print());

		}

	}

	@Nested
	class Patch {

		@Test
		@DisplayName("Should return updated user when valid Username and data is provided")
		void should_ReturnUpdatedUser_whenValidUsernameAndDataIsProvided() throws Exception {
			// Given
			UserInfoReqDto dto = new UserInfoReqDto(
				"Changed Username",
				"         "
			);
			String expectedStatus = "OK";
			int expectedCode = HttpStatus.OK.value();
			String expectedMessage = "User patched successfully";
			String expectedResponse = """
				{
				"status":"OK",
				"code":200,
				"message":"User patched successfully",
				"data":{"username":"Changed Username"}
				}
				""";

			// When

			ResultActions response = mockMvc.perform(
				patch("/api/v1/users/JohnJohn")
					.with(csrf())
					.with(oidcLogin().idToken(token -> token.claim("email", "email@email.com")))
					.content(jsonConverterAdapter.valueOf(dto))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
			);


			// Then
			response
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value(expectedStatus))
				.andExpect(jsonPath("$.code").value(expectedCode))
				.andExpect(jsonPath("$.message").value(expectedMessage))
				.andExpect(content().json(expectedResponse))
				.andDo(print());

		}

	}
}