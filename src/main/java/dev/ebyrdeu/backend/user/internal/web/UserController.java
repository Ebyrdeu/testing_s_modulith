package dev.ebyrdeu.backend.user.internal.web;

import dev.ebyrdeu.backend.common.dto.ResDto;
import dev.ebyrdeu.backend.common.util.JsonConverterAdapter;
import dev.ebyrdeu.backend.user.UserService;
import dev.ebyrdeu.backend.user.internal.infrastructure.projection.UserMinimalInfoProjection;
import dev.ebyrdeu.backend.user.internal.web.dto.UsernameReqDto;
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
	private final UserService userService;
	private final JsonConverterAdapter jsonConverter;

	public UserController(UserService userService, JsonConverterAdapter jsonConverter) {
		this.userService = userService;
		this.jsonConverter = jsonConverter;
	}

	@GetMapping
	public ResponseEntity<ResDto<List<UserMinimalInfoProjection>>> findAll() {

		log.info("[UserController/findAll]:: Fetching all users.");

		ResDto<List<UserMinimalInfoProjection>> response = this.userService.finalAll();

		log.info("[UserController/findAll]:: Response: {}", this.jsonConverter.valueOf(response.data()));

		return ResponseEntity.status(response.status()).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResDto<UserMinimalInfoProjection>> findOne(@PathVariable Long id) {
		log.info("[UserController/findOne]:: Fetching user with ID: {}", id);

		ResDto<UserMinimalInfoProjection> response = this.userService.findOneById(id);

		log.info(
			"[UserController/findOne]:: ID: {} | Response: {}",
			id,
			this.jsonConverter.valueOf(response)
		);

		return ResponseEntity.status(response.status()).body(response);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ResDto<UsernameReqDto>> patch(
		@PathVariable
		Long id,

		@RequestBody
		@Valid
		UsernameReqDto dto
	) {
		log.info(
			"[UserController/patch]:: ID: {} | Request body: {}",
			id,
			this.jsonConverter.valueOf(dto)
		);

		ResDto<UsernameReqDto> response = this.userService.patchUsername(id, dto);

		log.info(
			"[UserController/patch]:: ID: {} | Response: {}",
			id,
			this.jsonConverter.valueOf(response)
		);

		return ResponseEntity.status(response.status()).body(response);
	}


}
