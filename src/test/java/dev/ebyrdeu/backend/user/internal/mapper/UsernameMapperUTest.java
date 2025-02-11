package dev.ebyrdeu.backend.user.internal.mapper;

import dev.ebyrdeu.backend.user.internal.dto.UsernameDto;
import dev.ebyrdeu.backend.user.internal.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Username Mapper Unit Tests")
class UsernameMapperUTest {
	private UsernameDto createValidUsernameDto() {
		return new UsernameDto(
			"Valid Username"
		);
	}

	private User createValidUserEntity() {
		User entity = new User();
		entity.setUsername("Valid Username");

		return entity;
	}

	@Test
	@DisplayName("Should map User entity to UsernameDto when given a valid entity")
	void should_MapUserEntityToUsernameDto_whenGivenValidEntity() {
		// Given
		User entity = createValidUserEntity();

		// When
		UsernameDto dto = UsernameMapper.map(entity);

		// Then
		assertAll(
			() -> assertNotNull(dto),
			() -> assertEquals("Valid Username", dto.username())
		);
	}

	@Test
	@DisplayName("Should map UsernameDto to User entity when given a valid DTO")
	void should_MapUsernameDtoToUserEntity_whenGivenValidDto() {
		// Given
		UsernameDto dto = createValidUsernameDto();

		// When
		User entity = UsernameMapper.map(dto);

		// Then

		assertAll(
			() -> assertNotNull(entity),
			() -> assertEquals("Valid Username", entity.getUsername())
		);

	}

}