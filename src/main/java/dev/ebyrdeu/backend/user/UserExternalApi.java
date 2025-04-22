package dev.ebyrdeu.backend.user;

import dev.ebyrdeu.backend.common.dto.BaseResponseDto;
import dev.ebyrdeu.backend.user.internal.dto.AuthResponseDto;
import dev.ebyrdeu.backend.user.internal.dto.AuthUserDto;
import dev.ebyrdeu.backend.user.internal.dto.UsernameDto;
import dev.ebyrdeu.backend.user.internal.excpetion.UserInternalServerErrorException;
import dev.ebyrdeu.backend.user.internal.excpetion.UserNotFoundException;
import dev.ebyrdeu.backend.user.internal.projection.UserMinimalInfoProjection;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;

/**
 * API that can be used by different modules if needed
 *
 * @author Maxim Khnykin
 * @version 1.0
 */
public interface UserExternalApi {

	/**
	 * Retrieves authentication data for the currently authenticated user.
	 * <p>
	 * If the provided {@code authentication} is {@code null} or not authenticated, a response with an empty
	 * {@link AuthUserDto} is returned. Otherwise, the user's email and roles are extracted from the OIDC user,
	 * and additional user information is fetched from the database.
	 * </p>
	 *
	 * @param authentication the authentication object representing the current user session.
	 * @return a {@link  BaseResponseDto} containing an {@link  AuthResponseDto} with user authentication data and
	 * a flag indicating whether the user is authenticated.
	 * @throws UserNotFoundException            if the user with the given email is not found.
	 * @throws UserInternalServerErrorException if an unexpected error occurs during processing.
	 */
	BaseResponseDto<AuthResponseDto> getAuth(Authentication authentication);

	/**
	 * Retrieves all users with minimal information.
	 *
	 * @return a {@link BaseResponseDto} containing a list of {@link UserMinimalInfoProjection} objects representing all users.
	 * @throws UserInternalServerErrorException if an unexpected error occurs during retrieval.
	 */
	BaseResponseDto<List<UserMinimalInfoProjection>> findAll();

	/**
	 * Retrieves a single user by their unique identifier.
	 *
	 * @param username the unique identifier of the user.
	 * @return a {@link BaseResponseDto} containing a {@link UserMinimalInfoProjection} for the specified user.
	 * @throws UserNotFoundException            if the user with the given ID is not found.
	 * @throws UserInternalServerErrorException if an unexpected error occurs during retrieval.
	 */
	BaseResponseDto<UserMinimalInfoProjection> findOneByUsername(String username);

	/**
	 * Updates the username of a user identified by their unique ID.
	 * <p>
	 * The method retrieves the user from the database, and if the provided {@link  UsernameDto} contains a non-null username,
	 * it updates the user's username. The updated user is then saved back to the database.
	 * </p>
	 *
	 * @param id  the unique identifier of the user to update.
	 * @param req the {@link UsernameDto} containing the new username.
	 * @return a {@link BaseResponseDto} containing a {@link UsernameDto} of the updated user.
	 * @throws UserNotFoundException            if the user with the given ID is not found.
	 * @throws UserInternalServerErrorException if an unexpected error occurs during the update.
	 */
	BaseResponseDto<UsernameDto> patchUsername(Long id, UsernameDto req);

	/**
	 * Retrieves the roles associated with a user based on their email address.
	 *
	 * @param email the email address of the user.
	 * @return a list of role names associated with the user.
	 * @throws UserInternalServerErrorException if an unexpected error occurs during retrieval.
	 */
	List<String> findUserRolesByEmail(String email);

	/**
	 * Retrieves an existing OIDC user or creates a new one if not found in the database.
	 * <p>
	 * This method checks if a user with the OIDC user's email already exists. If the user does not exist, a new user is created
	 * using information from the provided {@code OidcUser}. The new user is saved, and a default role is assigned.
	 * </p>
	 *
	 * @param oidcUser the {@link  OidcUser} object containing user information from the OIDC provider.
	 * @throws UserInternalServerErrorException if an unexpected error occurs during user retrieval or creation.
	 */
	void createOrGetOidcUser(OidcUser oidcUser);

}
