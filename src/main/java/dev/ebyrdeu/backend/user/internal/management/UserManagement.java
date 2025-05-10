package dev.ebyrdeu.backend.user.internal.management;

import dev.ebyrdeu.backend.common.dto.BaseResponseDto;
import dev.ebyrdeu.backend.common.dto.BaseResponseJsonDto;
import dev.ebyrdeu.backend.common.util.Utils;
import dev.ebyrdeu.backend.user.UserExternalApi;
import dev.ebyrdeu.backend.user.internal.dto.AuthResponseDto;
import dev.ebyrdeu.backend.user.internal.dto.AuthUserDto;
import dev.ebyrdeu.backend.user.internal.dto.UserInfoReqDto;
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
 */
@Service
class UserManagement implements UserExternalApi {
	private static final Logger log = LoggerFactory.getLogger(UserManagement.class);
	private final Utils utils;
	private final UserRepository userRepository;

	public UserManagement(UserRepository userRepository, Utils utils) {
		this.userRepository = userRepository;
		this.utils = utils;
	}


	@Override
	@Transactional(readOnly = true)
	public BaseResponseDto<AuthResponseDto> getAuth(Authentication authentication) {
		log.debug("[UserManagement/getAuth]:: Execution started.");
		try {
			if (authentication == null || !authentication.isAuthenticated()) {
				log.trace("[UserManagement/getAuth]:: Anonymous request detected");
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

			log.debug("[UserManagement/getAuth]:: User found. Email: {}", email);

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
			log.error("[UserManagement/getAuth]:: User lookup failed. Message: {}", ex.getMessage());
			throw ex;
		} catch (RuntimeException ex) {
			log.error("[UserManagement/getAuth]:: Unexpected error. Message: {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to retrieve auth data due to an unexpected error");
		} finally {
			log.debug("[UserManagement/getAuth]:: Execution completed.");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public BaseResponseDto<List<UserMinimalInfoProjection>> findAll() {
		log.debug("[UserManagement/findAll]:: Execution started.");
		try {
			List<UserMinimalInfoProjection> data = this.userRepository.findAllWithMinimalInfo();
			log.debug("[UserManagement/findAll]:: Found {} user records", data.size());

			return new BaseResponseDto<>(
				HttpStatus.OK,
				HttpStatus.OK.value(),
				"Users retrieved successfully",
				data
			);
		} catch (RuntimeException ex) {
			log.error("[UserManagement/findAll]:: Database error. Message: {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to retrieve users due to an unexpected error");
		} finally {
			log.debug("[UserManagement/findAll]:: Execution completed.");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public BaseResponseJsonDto findOneByUsername(String username) {
		log.debug("[UserManagement/findOneById]:: Execution started. ID: {}", username);
		try {
			String data = this.userRepository
				.findOneByUsernameWithImages(username)
				.orElseThrow(
					() -> new UserNotFoundException("User with ID " + username + " not found")
				);

			log.debug("[UserManagement/findOneById]:: Found user. ID: {}", username);

			return new BaseResponseJsonDto(
				HttpStatus.OK,
				HttpStatus.OK.value(),
				"User retrieved successfully",
				data
			);
		} catch (UserNotFoundException ex) {
			log.error("[UserManagement/findOneById]:: Lookup failed. ID: {} | Message: {}", username, ex.getMessage());
			throw ex;
		} catch (RuntimeException ex) {
			log.error("[UserManagement/findOneById]:: Database error. Message: {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to retrieve user due to an unexpected error");
		} finally {
			log.debug("[UserManagement/findOneById]:: Execution completed.");
		}
	}

	@Override
	@Transactional
	public BaseResponseDto<UserInfoReqDto> patchUserInfo(String username, UserInfoReqDto req) {
		log.debug("[UserManagement/patchUsername]:: Execution started. Username: {}", username);
		try {
			User retrievedUser = this.userRepository
				.findOneByUsername(username)
				.orElseThrow(
					() -> new UserNotFoundException("User with Username " + username + " not found")
				);

			log.debug("[UserManagement/patchUsername]:: Retrieved user. Username: {}", username);

			this.utils.isStringPatchable(req.username(), retrievedUser::setUsername, true);
			this.utils.isStringPatchable(req.aboutMe(), retrievedUser::setAboutMe);

			User updatedUser = this.userRepository.save(retrievedUser);
			log.trace("[UserManagement/patchUsername]:: Updated username: {}", updatedUser.getUsername());

			return new BaseResponseDto<>(
				HttpStatus.OK,
				HttpStatus.OK.value(),
				"User patched successfully",
				UsernameMapper.map(updatedUser)
			);
		} catch (UserNotFoundException ex) {
			log.error("[UserManagement/patchUsername]:: Update failed. Username: {} | Message: {}", username, ex.getMessage());
			throw ex;
		} catch (RuntimeException ex) {
			log.error("[UserManagement/patchUsername]:: Database error. Message: {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to patch user due to an unexpected error");
		} finally {
			log.debug("[UserManagement/patchUsername]:: Execution completed.");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findUserRolesByEmail(String email) {
		log.debug("[UserManagement/findUserRolesByEmail]:: Execution started. Email: {}", email);
		try {
			List<String> data = this.userRepository.findRolesByEmail(email);
			log.trace("[UserManagement/findUserRolesByEmail]:: Roles found: {}", data);
			return data;
		} catch (RuntimeException ex) {
			log.error("[UserManagement/findUserRolesByEmail]:: Role lookup failed. Message: {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to retrieve user roles due to an unexpected error");
		} finally {
			log.debug("[UserManagement/findUserRolesByEmail]:: Execution completed.");
		}
	}

	@Override
	@Transactional
	public void createOrGetOidcUser(OidcUser oidcUser) {
		log.debug("[UserManagement/createOrGetOidcUser]:: Execution started.");
		try {
			log.debug("[UserManagement/createOrGetOidcUser]:: Checking existing user for email: {}", oidcUser.getEmail());
			if (this.userRepository.findOneByEmail(oidcUser.getEmail()).isPresent()) {
				return;
			}

			User user = new User();
			user.setEmail(oidcUser.getEmail());
			user.setFirstName(oidcUser.getGivenName());
			user.setLastName(oidcUser.getFamilyName());
			user.setUsername(oidcUser.getSubject());

			log.debug("[UserManagement/createOrGetOidcUser]:: Creating new user: {}", user.getEmail());
			User createdUser = this.userRepository.save(user);

			log.debug("[UserManagement/createOrGetOidcUser]:: Assigning default role to user: {}", createdUser.getId());
			this.userRepository.addSingleRole(createdUser.getId(), Role.USER.getRoleId());

		} catch (RuntimeException ex) {
			log.error("[UserManagement/createOrGetOidcUser]:: User creation failed. Message: {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to create OIDC user due to an unexpected error");
		} finally {
			log.debug("[UserManagement/createOrGetOidcUser]:: Execution completed.");
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