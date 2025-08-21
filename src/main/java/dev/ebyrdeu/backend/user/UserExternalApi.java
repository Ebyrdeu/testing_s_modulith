package dev.ebyrdeu.backend.user;

import dev.ebyrdeu.backend.user.internal.excpetion.UserInternalServerErrorException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;

/**
 * External API for user-related operations
 *
 * @author Maxim Khnykin
 * @version 1.0
 */
public interface UserExternalApi {

	/**
	 * Fetches role names for a user by their email.
	 *
	 * @param email the user's email address
	 * @return a list of role names (e.g. "USER", "ADMIN")
	 * @throws UserInternalServerErrorException if retrieval fails unexpectedly
	 */
	List<String> findUserRolesByEmail(String email);

	/**
	 * Retrieves an existing OIDC user or creates a new one if not found in the database.
	 * <p>
	 * If no user exists with the OIDC email, a new {@link dev.ebyrdeu.backend.user.internal.model.User}
	 * is created using the OIDC claims (subject as username, given/family names, email)
	 * and assigned a default role.
	 * </p>
	 *
	 * @param oidcUser the authenticated {@link OidcUser} from the OIDC provider
	 * @throws UserInternalServerErrorException if creation or lookup fails unexpectedly
	 */
	void createOrGetOidcUser(OidcUser oidcUser);

}
