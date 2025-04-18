package dev.ebyrdeu.backend.user.internal.management;

import dev.ebyrdeu.backend.common.dto.BaseResponseDto;
import dev.ebyrdeu.backend.user.UserExternalApi;
import dev.ebyrdeu.backend.user.internal.dto.AuthResponseDto;
import dev.ebyrdeu.backend.user.internal.dto.AuthUserDto;
import dev.ebyrdeu.backend.user.internal.dto.UsernameDto;
import dev.ebyrdeu.backend.user.internal.excpetion.UserInternalServerErrorException;
import dev.ebyrdeu.backend.user.internal.excpetion.UserNotFoundException;
import dev.ebyrdeu.backend.user.internal.mapper.UsernameMapper;
import dev.ebyrdeu.backend.user.internal.model.User;
import dev.ebyrdeu.backend.user.internal.projection.UserMinimalInfoProjection;
import dev.ebyrdeu.backend.user.internal.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Maxim Knhykin
 * @version 1.0
 * @see UserExternalApi
 * @see UserRepository
 * @see UserMinimalInfoProjection
 * @see UserNotFoundException
 * @see UserInternalServerErrorException
 */
@Service
class UserManagement implements UserExternalApi {
	private static final Logger log = LoggerFactory.getLogger(UserManagement.class);
	private final UserRepository userRepository;

	public UserManagement(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public BaseResponseDto<AuthResponseDto> getAuth(Authentication authentication) {
		log.debug("[UserManagement/getAuth]:: Execution started.");
		try {
			if (authentication == null || !authentication.isAuthenticated()) {
				log.trace("[UserManagement/getAuth]:: Anonymous request");
				return new BaseResponseDto<>(
					HttpStatus.OK,
					HttpStatus.OK.value(),
					"Authentication data retrieved successfully",
					new AuthResponseDto(null, false)
				);
			}

			OidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
			String email = oidcUser.getEmail();
			List<String> roles = oidcUser.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.map(role -> role.substring(5))
				.toList();

			UserMinimalInfoProjection res = this.userRepository
				.findOneByEmailWithMinimalInfo(email)
				.orElseThrow(
					() -> new UserNotFoundException("User with Email " + email + " not found")
				);

			log.info("[UserManagement/getAuth]:: User found. User Email: {}", email);

			AuthUserDto data = new AuthUserDto(
				res.getFirstName(),
				res.getLastName(),
				res.getUsername(),
				oidcUser.getPicture(),
				roles
			);

			return new BaseResponseDto<>(
				HttpStatus.OK,
				HttpStatus.OK.value(),
				"Authentication data retrieved successfully",
				new AuthResponseDto(data, true)
			);

		} catch (UserNotFoundException ex) {
			log.error(
				"[UserManagement/getAuth]:: Exception occurred while retrieving user. Exception: {}",
				ex.getMessage()
			);
			throw ex;
		} catch (RuntimeException ex) {
			log.error("[UserManagement/getAuth]:: Exception occurred while  auth data. Exception: {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to retrieve auth data due to an unexpected error");
		} finally {
			log.info("[UserManagement/getAuth]:: Execution ended.");
		}
	}

	/**
	 * Retrieves all users with minimal information.
	 *
	 * @return a {@link BaseResponseDto} containing a list of {@link UserMinimalInfoProjection} objects representing all users.
	 * @throws UserInternalServerErrorException if an unexpected error occurs during retrieval.
	 */
	@Override
	@Transactional(readOnly = true)
	public BaseResponseDto<List<UserMinimalInfoProjection>> findAll() {
		log.info("[UserManagement/findAll]:: Execution started.");
		try {
			List<UserMinimalInfoProjection> data = this.userRepository.findAllWithMinimalInfo();
			log.info("[UserManagement/findAll]:: Found {} users.", data.size());

			return new BaseResponseDto<>(
				HttpStatus.OK,
				HttpStatus.OK.value(),
				"Users retrieved successfully",
				data
			);
		} catch (RuntimeException ex) {
			log.error("[UserManagement/findAll]:: Exception occurred while retrieving users. Exception: {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to retrieve users due to an unexpected error");
		} finally {
			log.info("[UserManagement/findAll]:: Execution ended.");
		}

	}

	@Override
	@Transactional(readOnly = true)
	public BaseResponseDto<UserMinimalInfoProjection> findOneById(Long id) {
		log.info("[UserManagement/findOneById]:: Execution started.");

		try {
			UserMinimalInfoProjection data = this.userRepository
				.findOneByIdWithMinimalInfo(id)
				.orElseThrow(
					() -> new UserNotFoundException("User with ID " + id + " not found")
				);

			log.info("[UserManagement/findOneById]:: User found. User ID: {}", id);

			return new BaseResponseDto<>(
				HttpStatus.OK,
				HttpStatus.OK.value(),
				"User retrieved successfully",
				data
			);
		} catch (UserNotFoundException ex) {
			log.error(
				"[UserManagement/findOneById]:: Exception occurred while retrieving user. User ID : {}. Exception: {}",
				id,
				ex.getMessage()
			);
			throw ex;
		} catch (RuntimeException ex) {
			log.error(
				"[UserManagement/findOneById]:: Exception occurred while retrieving user from database , Exception message {}",
				ex.getMessage()
			);
			throw new UserInternalServerErrorException("Failed to retrieve user due to an unexpected error");
		} finally {
			log.info("[UserManagement/findOneById]:: Execution ended.");
		}


	}

	@Override
	@Transactional
	public BaseResponseDto<UsernameDto> patchUsername(Long id, UsernameDto req) {
		log.info("[UserManagement/patchUsername]:: Execution started.");
		try {
			User retrievedUser = this.userRepository
				.findById(id)
				.orElseThrow(
					() -> new UserNotFoundException("User with ID " + id + " not found")
				);

			log.info("[UserManagement/patchUsername]:: User found. User ID: {}", id);

			if (req.username() != null) {
				retrievedUser.setUsername(req.username());
			}

			User updatedUser = this.userRepository.save(retrievedUser);

			log.info("[UserManagement/patchUsername]:: User patched successfully. User ID: {}", id);
			return new BaseResponseDto<>(
				HttpStatus.OK,
				HttpStatus.OK.value(),
				"User patched successfully",
				UsernameMapper.map(updatedUser)
			);
		} catch (UserNotFoundException ex) {
			log.error(
				"[UserManagement/patchUsername]:: Exception occurred while retrieving user. User ID : {}. Exception: {}",
				id,
				ex.getMessage()
			);
			throw ex;
		} catch (RuntimeException ex) {
			log.error("[UserManagement/patchUsername]:: Exception occurred while patching user to database , Exception message {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to patch user due to an unexpected error");
		} finally {
			log.info("[UserManagement/patchUsername]:: Execution ended.");
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findUserRolesByEmail(String email) {
		log.info("[UserManagement/findUserRolesByEmail]:: Execution started.");
		try {
			List<String> data = this.userRepository.findRolesByEmail(email);
			log.info("[UserManagement/findUserRolesByEmail]:: Found {} user roles :: {}", data.size(), data);

			return data;
		} catch (RuntimeException ex) {
			log.error("[UserManagement/findUserRolesByEmail]:: Exception occurred while retrieving user roles. Exception: {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to retrieve user roles due to an unexpected error");
		} finally {
			log.info("[UserManagement/findUserRolesByEmail]:: Execution ended.");
		}
	}

	@Override
	@Transactional
	public void createOrGetOidcUser(OidcUser oidcUser) {
		log.info("[UserManagement/createOrGetOidcUser]:: Execution started.");
		try {

			log.info("[UserManagement/createOrGetOidcUser]:: checking existing users");
			if (this.userRepository.findOneByEmail(oidcUser.getEmail()).isPresent()) {
				return;
			}

			User user = new User();
			user.setEmail(oidcUser.getEmail());
			user.setFirstName(oidcUser.getGivenName());
			user.setLastName(oidcUser.getFamilyName());
			user.setUsername(oidcUser.getSubject());


			log.info("[UserManagement/createOrGetOidcUser]:: Saving new user :: {} to db ", user);
			User createdUser = this.userRepository.save(user);

			log.info("[UserManagement/createOrGetOidcUser]:: Adding default role for user :: {}", createdUser);
			this.userRepository.addSingleRole(createdUser.getId(), Role.USER.getRoleId());

		} catch (RuntimeException ex) {
			log.error("[UserManagement/createOrGetOidcUser]:: Exception occurred while retrieving or creating user. Exception: {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to retrieve user roles due to an unexpected error");
		} finally {
			log.info("[UserManagement/createOrGetOidcUser]:: Execution ended.");
		}
	}

	/**
	 * Value set in that way because we generate roles in exact this order and just check their id
	 * <p>
	 * Database schema: src/main/resources/db/changelog/table/user_role/db.changelog.user_role-1.0.xml
	 */
	private enum Role {
		USER(1L),
		VENDOR(2L),
		ADMIN(3L);


		private final Long roleId;

		Role(Long roleId) {
			this.roleId = roleId;
		}

		public Long getRoleId() {
			return roleId;
		}
	}

}