package dev.ebyrdeu.backend.user;

import dev.ebyrdeu.backend.common.dto.ResponseDto;
import dev.ebyrdeu.backend.user.internal.dto.UsernameDto;
import dev.ebyrdeu.backend.user.internal.projection.UserMinimalInfoProjection;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;

/**
 * @author Maxim Khnykin
 * @version 1.0
 */
public interface UserExternalApi {

	ResponseDto<List<UserMinimalInfoProjection>> finalAll();

	ResponseDto<UserMinimalInfoProjection> findOneById(Long id);

	/**
	 * Updates the username of a user with the specified ID.
	 * The new username is provided in the {@link UsernameDto} request body.
	 *
	 * @param id  the ID of the user whose username will be updated
	 * @param req the {@link UsernameDto} containing the new username
	 * @return a {@link ResponseDto} containing the updated {@link UsernameDto} if the operation is successful,
	 * or an appropriate error response if the user is not found or the update fails
	 */
	ResponseDto<UsernameDto> patchUsername(Long id, UsernameDto req);

	/**
	 * Retrieves a list of roles associated with a user by their email address.
	 * This method returns a raw list of role names (as strings) without additional metadata.
	 *
	 * @param email the email address of the user
	 * @return a list of role names (as strings) associated with the user,
	 * or an empty list if no roles are found for the given email
	 */
	List<String> findUserRolesByEmail(String email);

	void createOrGetOidcUser(OidcUser oidcUser);
}
