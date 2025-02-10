package dev.ebyrdeu.backend.user.internal.management;

import dev.ebyrdeu.backend.common.dto.ResponseDto;
import dev.ebyrdeu.backend.user.UserExternalApi;
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
	public ResponseDto<List<UserMinimalInfoProjection>> finalAll() {
		log.info("[UserManagement/findAll]:: Execution started.");
		try {
			List<UserMinimalInfoProjection> data = this.userRepository.findAllWithMinimalInfo();
			log.info("[UserManagement/findAll]:: Found {} users.", data.size());

			return new ResponseDto<>(
				HttpStatus.OK,
				HttpStatus.OK.value(),
				"Users retrieved successfully",
				data
			);
		} catch (UserInternalServerErrorException ex) {
			log.error("[UserManagement/findAll]:: Exception occurred while retrieving users. Exception: {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to retrieve users due to an unexpected error");
		} finally {
			log.info("[UserManagement/findAll]:: Execution ended.");
		}

	}

	@Override
	@Transactional(readOnly = true)
	public ResponseDto<UserMinimalInfoProjection> findOneById(Long id) {
		log.info("[UserManagement/findOneById]:: Execution started.");

		try {
			UserMinimalInfoProjection data = this.userRepository
				.findOneByIdWithMinimalInfo(id)
				.orElseThrow(
					() -> new UserNotFoundException("User with ID " + id + " not found")
				);

			log.info("[UserManagement/findOneById]:: User found. User ID: {}", id);

			return new ResponseDto<>(
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
		} catch (UserInternalServerErrorException ex) {
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
	public ResponseDto<UsernameDto> patchUsername(Long id, UsernameDto req) {
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
			return new ResponseDto<>(
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
		} catch (UserInternalServerErrorException ex) {
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
		} catch (UserInternalServerErrorException ex) {
			log.error("[UserManagement/findUserRolesByEmail]:: Exception occurred while retrieving user roles. Exception: {}", ex.getMessage());
			throw new UserInternalServerErrorException("Failed to retrieve user roles due to an unexpected error");
		} finally {
			log.info("[UserManagement/findUserRolesByEmail]:: Execution ended.");
		}
	}

	@Override
	@Transactional
	public void createOrGetOidcUser(OidcUser oidcUser) {
		if (this.userRepository.findOneByEmail(oidcUser.getEmail()).isPresent()) {
			return;
		}

		User user = new User();
		user.setEmail(oidcUser.getEmail());
		user.setFirstName(oidcUser.getGivenName());
		user.setLastName(oidcUser.getFamilyName());
		user.setUsername(oidcUser.getSubject());

		User createdUser = this.userRepository.save(user);

		// USER - 1
		// VENDOR - 2
		// ADMIN - 3
		// Unless you want to test staff - stick with 1
		this.userRepository.addSingleRole(createdUser.getId(), 1L);

	}

}
