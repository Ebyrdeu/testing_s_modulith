package dev.ebyrdeu.backend.user.internal.web;

import dev.ebyrdeu.backend.common.dto.ResponseDto;
import dev.ebyrdeu.backend.common.util.JsonConverterAdapter;
import dev.ebyrdeu.backend.user.UserExternalApi;
import dev.ebyrdeu.backend.user.internal.dto.UsernameDto;
import dev.ebyrdeu.backend.user.internal.projection.UserMinimalInfoProjection;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Maxim Khnykin
 * @version 1.0
 */
@Controller
@ResponseBody
@RequestMapping("/api/v1/users")
class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private final UserExternalApi userExternalApi;
	private final JsonConverterAdapter jsonConverter;

	public UserController(UserExternalApi userExternalApi, JsonConverterAdapter jsonConverter) {
		this.userExternalApi = userExternalApi;
		this.jsonConverter = jsonConverter;
	}

	@GetMapping
	public ResponseEntity<ResponseDto<List<UserMinimalInfoProjection>>> findAll() {

		log.info("[UserController/findAll]:: Fetching all users.");

		ResponseDto<List<UserMinimalInfoProjection>> response = this.userExternalApi.finalAll();

		log.info("[UserController/findAll]:: Response: {}", this.jsonConverter.valueOf(response.data()));

		return ResponseEntity.status(response.status()).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseDto<UserMinimalInfoProjection>> findOne(@PathVariable Long id) {
		log.info("[UserController/findOne]:: Fetching user with ID: {}", id);

		ResponseDto<UserMinimalInfoProjection> response = this.userExternalApi.findOneById(id);

		log.info(
			"[UserController/findOne]:: ID: {} | Response: {}",
			id,
			this.jsonConverter.valueOf(response)
		);

		return ResponseEntity.status(response.status()).body(response);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ResponseDto<UsernameDto>> patch(
		@PathVariable
		Long id,

		@RequestBody
		@Valid
		UsernameDto dto
	) {
		log.info(
			"[UserController/patch]:: ID: {} | Request body: {}",
			id,
			this.jsonConverter.valueOf(dto)
		);

		ResponseDto<UsernameDto> response = this.userExternalApi.patchUsername(id, dto);

		log.info(
			"[UserController/patch]:: ID: {} | Response: {}",
			id,
			this.jsonConverter.valueOf(response)
		);

		return ResponseEntity.status(response.status()).body(response);
	}


}
