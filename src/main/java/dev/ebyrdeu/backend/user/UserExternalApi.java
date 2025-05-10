package dev.ebyrdeu.backend.user;

import dev.ebyrdeu.backend.common.dto.BaseResponseDto;
import dev.ebyrdeu.backend.common.dto.BaseResponseJsonDto;
import dev.ebyrdeu.backend.user.internal.dto.AuthResponseDto;
import dev.ebyrdeu.backend.user.internal.dto.AuthUserDto;
import dev.ebyrdeu.backend.user.internal.dto.UserInfoReqDto;
import dev.ebyrdeu.backend.user.internal.excpetion.UserInternalServerErrorException;
import dev.ebyrdeu.backend.user.internal.excpetion.UserNotFoundException;
import dev.ebyrdeu.backend.user.internal.projection.UserMinimalInfoProjection;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;

/**
 * External API for user-related operations, exposing endpoints for authentication,
 * user retrieval and updates, and role management.
 * <p>
 * Implementations of this interface serve as the facade for external modules
 * to interact with user data without exposing internal persistence details.
 * </p>
 *
 * @author Maxim Khnykin
 * @version 1.0
 */
public interface UserExternalApi {

	/**
	 * Returns authentication details for the current user.
	 * <p>
	 * If the provided {@code authentication} object is null or unauthenticated,
	 * an empty {@link AuthUserDto} is returned with {@code authenticated=false}.
	 * Otherwise, extracts the email and roles from the OIDC user, loads
	 * minimal user info from the database, and wraps it in an {@link AuthResponseDto}.
	 * </p>
	 *
	 * @param authentication the security context of the current request
	 * @return a {@link BaseResponseDto} containing an {@link AuthResponseDto}
	 * with user details and authentication status
	 * @throws UserNotFoundException            if no user is found for the given email
	 * @throws UserInternalServerErrorException if an unexpected error occurs
	 */
	BaseResponseDto<AuthResponseDto> getAuth(Authentication authentication);

	/**
	 * Retrieves all users with minimal profile information.
	 * <p>
	 * Each user is represented by a {@link UserMinimalInfoProjection} containing
	 * only username, first name and last name.
	 * </p>
	 *
	 * @return a {@link BaseResponseDto} wrapping a list of minimal user projections
	 * @throws UserInternalServerErrorException if retrieval fails unexpectedly
	 */
	BaseResponseDto<List<UserMinimalInfoProjection>> findAll();

	/**
	 * Retrieves a single user's detailed profile and associated images as JSON.
	 * <p>
	 * The response DTO wraps a raw JSON string payload that includes username,
	 * first name, last name, email and an array of images (title, description,
	 * price, and URL). If the user has no images, the array is empty.
	 * </p>
	 *
	 * @param username the unique username identifier
	 * @return a {@link BaseResponseJsonDto} containing the JSON payload
	 * @throws UserNotFoundException            if no user is found for the given username
	 * @throws UserInternalServerErrorException if retrieval fails unexpectedly
	 */
	BaseResponseJsonDto findOneByUsername(String username);

	/**
	 * Updates a user's information.
	 * <p>
	 * This method performs a partial update (patch) on the user identified by {@code username},
	 * applying only the non-null or non-empty fields from the provided {@link UserInfoReqDto}.
	 * Specifically:
	 * <ul>
	 *   <li>{@code username} field in {@code req} is applied only if non-null and non-empty.</li>
	 *   <li>{@code aboutMe} field in {@code req} is applied only if non-null.</li>
	 * </ul>
	 * The updated user is saved in the repository, and a response DTO is returned containing
	 * the updated user information.
	 * </p>
	 *
	 * @param username the username of the user to update
	 * @param req      a {@link UserInfoReqDto} containing patch data (new username, about me)
	 * @return a {@link BaseResponseDto} wrapping the updated {@link UserInfoReqDto}
	 * @throws UserNotFoundException            if no user is found for the given username
	 * @throws UserInternalServerErrorException if the update fails due to an unexpected error
	 */
	BaseResponseDto<UserInfoReqDto> patchUserInfo(String username, UserInfoReqDto req);

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
