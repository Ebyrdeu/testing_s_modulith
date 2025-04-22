package dev.ebyrdeu.backend.user.internal.web;

import dev.ebyrdeu.backend.common.dto.BaseResponseDto;
import dev.ebyrdeu.backend.common.util.JsonConverterAdapter;
import dev.ebyrdeu.backend.user.UserExternalApi;
import dev.ebyrdeu.backend.user.internal.dto.AuthResponseDto;
import dev.ebyrdeu.backend.user.internal.dto.UsernameDto;
import dev.ebyrdeu.backend.user.internal.projection.UserMinimalInfoProjection;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private final UserExternalApi userExternalApi;
	private final JsonConverterAdapter jsonConverter;

	public UserController(UserExternalApi userExternalApi, JsonConverterAdapter jsonConverter) {
		this.userExternalApi = userExternalApi;
		this.jsonConverter = jsonConverter;
	}

	// TODO: Create ITests
	@GetMapping("/auth")
	public ResponseEntity<BaseResponseDto<AuthResponseDto>> getAuth(Authentication authentication) {
		log.debug("[UserController/auth]:: Getting auth status.");

		BaseResponseDto<AuthResponseDto> response = this.userExternalApi.getAuth(authentication);

		log.trace("[UserController/auth]:: Response data: {}", this.jsonConverter.valueOf(response.data()));

		return ResponseEntity.status(response.status()).body(response);
	}

	@GetMapping
	public ResponseEntity<BaseResponseDto<List<UserMinimalInfoProjection>>> findAllWithMinimalInfo() {
		log.debug("[UserController/findAll]:: Fetching all users.");

		BaseResponseDto<List<UserMinimalInfoProjection>> response = this.userExternalApi.findAll();

		log.trace("[UserController/findAll]:: Response data: {}", this.jsonConverter.valueOf(response.data()));

		return ResponseEntity.status(response.status()).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BaseResponseDto<UserMinimalInfoProjection>> findOneUserWithMinimalInfo(@PathVariable Long id) {
		log.debug("[UserController/findOne]:: Fetching user with ID: {}", id);

		BaseResponseDto<UserMinimalInfoProjection> response = this.userExternalApi.findOneById(id);

		log.trace("[UserController/findOne]:: ID: {} | Response data: {}", id, this.jsonConverter.valueOf(response.data()));

		return ResponseEntity.status(response.status()).body(response);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<BaseResponseDto<UsernameDto>> updateUsername(
		@PathVariable Long id,
		@RequestBody @Valid UsernameDto dto
	) {
		log.debug("[UserController/patch]:: ID: {}", id);

		log.trace("[UserController/patch]:: ID: {} | Request body: {}", id, this.jsonConverter.valueOf(dto));

		BaseResponseDto<UsernameDto> response = this.userExternalApi.patchUsername(id, dto);

		log.debug("[UserController/patch]:: ID: {} | Response status: {}", id, response.status());

		log.trace("[UserController/patch]:: ID: {} | Response data: {}", id, this.jsonConverter.valueOf(response.data()));

		return ResponseEntity.status(response.status()).body(response);
	}
}