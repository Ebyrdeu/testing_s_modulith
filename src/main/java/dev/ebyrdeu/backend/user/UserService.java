package dev.ebyrdeu.backend.user;

import dev.ebyrdeu.backend.common.dto.ResDto;
import dev.ebyrdeu.backend.user.internal.infrastructure.projection.UserMinimalInfoProjection;
import dev.ebyrdeu.backend.user.internal.web.dto.UsernameReqDto;

import java.util.List;

/**
 * @author Maxim Khnykin
 * @version 1.0
 */
public interface UserService {

	ResDto<List<UserMinimalInfoProjection>> finalAll();

	ResDto<UserMinimalInfoProjection> findOneById(Long id);

	ResDto<UsernameReqDto> patchUsername(Long id, UsernameReqDto requestedDto);

}
