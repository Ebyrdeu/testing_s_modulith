package dev.ebyrdeu.backend.user.internal.mapper;

import dev.ebyrdeu.backend.user.internal.dto.UsernameDto;
import dev.ebyrdeu.backend.user.internal.model.User;

/**
 * Mapper for {@link  UsernameDto}
 *
 * @author Maxim Khnykin
 * @version 1.0
 */
public class UsernameMapper {
	private UsernameMapper() {
		throw new UnsupportedOperationException("Mapper class - instantiation not allowed");
	}

	public static UsernameDto map(User entity) {
		if (entity == null) {
			return null;
		}

		return new UsernameDto(
			entity.getUsername()
		);
	}

	public static User map(UsernameDto dto) {
		if (dto == null) {
			return null;
		}

		User user = new User();
		user.setUsername(dto.username());

		return user;
	}

}
