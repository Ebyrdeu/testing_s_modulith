package dev.ebyrdeu.backend.user.internal.web;

import dev.ebyrdeu.backend.common.dto.ResDto;
import dev.ebyrdeu.backend.user.UserService;
import dev.ebyrdeu.backend.user.internal.infrastructure.projection.UserMinimalInfoProjection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Maxim Khnykin
 * @version 1.0
 */
@Controller
@ResponseBody
@RequestMapping("/api/v1/users")
class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResDto<List<UserMinimalInfoProjection>> findAll() {
		return userService.finalAll();
	}
}
