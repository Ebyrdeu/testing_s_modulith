package dev.ebyrdeu.backend.user.internal.mapper;

import dev.ebyrdeu.backend.user.internal.dto.UserInfoReqDto;
import dev.ebyrdeu.backend.user.internal.model.User;

/**
 * Mapper for {@link  UserInfoReqDto}
 *
 * @author Maxim Khnykin
 * @version 1.0
 * @see UserInfoReqDto
 */
public class UsernameMapper {
	private UsernameMapper() {
		throw new UnsupportedOperationException("Mapper class - instantiation not allowed");
	}

	public static UserInfoReqDto map(User entity) {
		if (entity == null) {
			return null;
		}

		return new UserInfoReqDto(
				entity.getUsername(),
				entity.getAboutMe()
		);
	}

	public static User map(UserInfoReqDto dto) {
		if (dto == null) {
			return null;
		}

		User user = new User();
		user.setUsername(dto.username());
		user.setAboutMe(dto.aboutMe());

		return user;
	}

}
