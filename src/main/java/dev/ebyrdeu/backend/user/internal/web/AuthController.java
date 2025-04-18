package dev.ebyrdeu.backend.user.internal.web;

import dev.ebyrdeu.backend.common.dto.BaseResponseDto;
import dev.ebyrdeu.backend.common.util.JsonConverterAdapter;
import dev.ebyrdeu.backend.user.UserExternalApi;
import dev.ebyrdeu.backend.user.internal.dto.AuthResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
class AuthController {
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	private final UserExternalApi userExternalApi;
	private final JsonConverterAdapter jsonConverter;

	public AuthController(UserExternalApi userExternalApi, JsonConverterAdapter jsonConverter) {
		this.userExternalApi = userExternalApi;
		this.jsonConverter = jsonConverter;
	}

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public ResponseEntity<BaseResponseDto<AuthResponseDto>> getAuth(Authentication authentication) {

		log.info("[UserController/auth]:: Getting auth status.");

		BaseResponseDto<AuthResponseDto> response = this.userExternalApi.getAuth(authentication);

		log.info("[UserController/auth]:: Response: {}", this.jsonConverter.valueOf(response.data()));

		return ResponseEntity.status(response.status()).body(response);
	}
}
