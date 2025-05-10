package dev.ebyrdeu.backend.user.internal.web;

import dev.ebyrdeu.backend.common.dto.BaseResponseDto;
import dev.ebyrdeu.backend.common.dto.BaseResponseJsonDto;
import dev.ebyrdeu.backend.common.util.JsonConverterAdapter;
import dev.ebyrdeu.backend.user.UserExternalApi;
import dev.ebyrdeu.backend.user.internal.dto.AuthResponseDto;
import dev.ebyrdeu.backend.user.internal.dto.UserInfoReqDto;
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

	@GetMapping("/{username}")
	public ResponseEntity<BaseResponseJsonDto> findOneUserWithMinimalInfo(@PathVariable String username) {
		log.debug("[UserController/findOne]:: Fetching user with Username: {}", username);

		BaseResponseJsonDto response = this.userExternalApi.findOneByUsername(username);

		log.trace("[UserController/findOne]:: Username: {} | Response data: {}", username, this.jsonConverter.valueOf(response.data()));

		return ResponseEntity.status(response.status()).body(response);
	}

	@PatchMapping("/{username}")
	public ResponseEntity<BaseResponseDto<UserInfoReqDto>> updateUserInfo(
			@PathVariable String username,
			@RequestBody @Valid UserInfoReqDto dto
	) {
		log.debug("[UserController/patch]:: Username: {}", username);

		log.trace("[UserController/patch]:: Username: {} | Request body: {}", username, this.jsonConverter.valueOf(dto));

		BaseResponseDto<UserInfoReqDto> response = this.userExternalApi.patchUserInfo(username, dto);

		log.debug("[UserController/patch]:: Username: {} | Response status: {}", username, response.status());

		log.trace("[UserController/patch]:: Username: {} | Response data: {}", username, this.jsonConverter.valueOf(response.data()));

		return ResponseEntity.status(response.status()).body(response);
	}
}