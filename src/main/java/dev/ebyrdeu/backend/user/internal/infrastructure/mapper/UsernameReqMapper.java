package dev.ebyrdeu.backend.user.internal.infrastructure.mapper;

import dev.ebyrdeu.backend.user.internal.infrastructure.entity.User;
import dev.ebyrdeu.backend.user.internal.web.dto.UsernameReqDto;

/**
 * Mapper for {@link  dev.ebyrdeu.backend.user.internal.web.dto.UsernameReqDto}
 *
 * @author Maxim Khnykin
 * @version 1.0
 */
public class UsernameReqMapper {
	private UsernameReqMapper() {
		throw new UnsupportedOperationException("Mapper class - instantiation not allowed");
	}

	public static UsernameReqDto map(User entity) {
		if (entity == null) {
			return null;
		}

		return new UsernameReqDto(
			entity.getUsername()
		);
	}

	public static User map(UsernameReqDto dto) {
		if (dto == null) {
			return null;
		}

		User user = new User();
		user.setUsername(dto.username());

		return user;
	}

}
