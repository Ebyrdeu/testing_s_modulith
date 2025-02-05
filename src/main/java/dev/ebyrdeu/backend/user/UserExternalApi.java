package dev.ebyrdeu.backend.user;

import dev.ebyrdeu.backend.common.dto.ResponseDto;
import dev.ebyrdeu.backend.user.internal.dto.UsernameDto;
import dev.ebyrdeu.backend.user.internal.projection.UserMinimalInfoProjection;

import java.util.List;

/**
 * @author Maxim Khnykin
 * @version 1.0
 */
public interface UserExternalApi {

	ResponseDto<List<UserMinimalInfoProjection>> finalAll();

	ResponseDto<UserMinimalInfoProjection> findOneById(Long id);

	ResponseDto<UsernameDto> patchUsername(Long id, UsernameDto requestedDto);

}
