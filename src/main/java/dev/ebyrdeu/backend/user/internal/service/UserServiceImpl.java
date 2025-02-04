package dev.ebyrdeu.backend.user.internal.service;

import dev.ebyrdeu.backend.common.dto.ResDto;
import dev.ebyrdeu.backend.user.UserService;
import dev.ebyrdeu.backend.user.internal.excpetion.UserNotFoundException;
import dev.ebyrdeu.backend.user.internal.excpetion.UserServiceException;
import dev.ebyrdeu.backend.user.internal.infrastructure.entity.User;
import dev.ebyrdeu.backend.user.internal.infrastructure.mapper.UsernameReqMapper;
import dev.ebyrdeu.backend.user.internal.infrastructure.projection.UserMinimalInfoProjection;
import dev.ebyrdeu.backend.user.internal.infrastructure.repository.UserRepository;
import dev.ebyrdeu.backend.user.internal.web.dto.UsernameReqDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class UserServiceImpl implements UserService {
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public ResDto<List<UserMinimalInfoProjection>> finalAll() {
		log.info("[UserRestService/findAll]:: Execution started.");
		try {
			List<UserMinimalInfoProjection> data = this.userRepository.findAllWithMinimalInfo();
			log.info("[UserRestService/findAll]:: Found {} users.", data.size());

			return new ResDto<>(
				HttpStatus.OK,
				HttpStatus.OK.value(),
				"Users retrieved successfully",
				data
			);
		} catch (RuntimeException ex) {
			log.error("[UserRestService/findAll]:: Exception occurred while retrieving users. Exception: {}", ex.getMessage());
			throw new UserServiceException("Failed to retrieve users due to an unexpected error");
		} finally {
			log.info("[UserRestService/findAll]:: Execution ended.");
		}

	}

	@Override
	@Transactional(readOnly = true)
	public ResDto<UserMinimalInfoProjection> findOneById(Long id) {
		log.info("[UserRestService/findOneById]:: Execution started.");

		try {
			UserMinimalInfoProjection data = this.userRepository
				.findOneByIdWithMinimalInfo(id)
				.orElseThrow(
					() -> new UserNotFoundException("User with ID " + id + " not found")
				);

			log.info("[UserRestService/findOneById]:: User found. User ID: {}", id);

			return new ResDto<>(
				HttpStatus.OK,
				HttpStatus.OK.value(),
				"User retrieved successfully",
				data
			);
		} catch (UserNotFoundException ex) {
			log.error(
				"[UserRestService/findOneById]:: Exception occurred while retrieving user. User ID : {}. Exception: {}",
				id,
				ex.getMessage()
			);
			throw ex;
		} catch (RuntimeException ex) {
			log.error(
				"[UserRestService/findOneById]:: Exception occurred while retrieving user from database , Exception message {}",
				ex.getMessage()
			);
			throw new UserServiceException("Failed to retrieve user due to an unexpected error");
		} finally {
			log.info("[UserRestService/findOneById]:: Execution ended.");
		}


	}

	@Override
	@Transactional
	public ResDto<UsernameReqDto> patchUsername(Long id, UsernameReqDto requestedDto) {
		log.info("[UserRestService/patchUsername]:: Execution started.");
		try {
			User retrievedUser = this.userRepository
				.findById(id)
				.orElseThrow(
					() -> new UserNotFoundException("User with ID " + id + " not found")
				);

			log.info("[UserRestService/patchUsername]:: User found. User ID: {}", id);

			if (requestedDto.username() != null) {
				retrievedUser.setUsername(requestedDto.username());
			}

			User updatedUser = this.userRepository.save(retrievedUser);

			log.info("[UserRestService/patchUsername]:: User patched successfully. User ID: {}", id);
			return new ResDto<>(
				HttpStatus.OK,
				HttpStatus.OK.value(),
				"User patched successfully",
				UsernameReqMapper.map(updatedUser)
			);
		} catch (UserNotFoundException ex) {
			log.error(
				"[UserRestService/patchUsername]:: Exception occurred while retrieving user. User ID : {}. Exception: {}",
				id,
				ex.getMessage()
			);
			throw ex;
		} catch (RuntimeException ex) {
			log.error("[UserRestService/patchUsername]:: Exception occurred while patching user to database , Exception message {}", ex.getMessage());
			throw new UserServiceException("Failed to patch user due to an unexpected error");
		} finally {
			log.info("[UserRestService/patchUsername]:: Execution ended.");
		}

	}
}
