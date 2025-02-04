package dev.ebyrdeu.backend.role.internal.service;

import dev.ebyrdeu.backend.role.RoleService;
import dev.ebyrdeu.backend.role.RoleType;
import dev.ebyrdeu.backend.role.internal.exception.RoleNotFoundException;
import dev.ebyrdeu.backend.role.internal.exception.RoleServiceException;
import dev.ebyrdeu.backend.role.internal.infrastructure.entity.Role;
import dev.ebyrdeu.backend.role.internal.infrastructure.repository.Rolerepository;
import dev.ebyrdeu.backend.user.internal.excpetion.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
class RoleServiceImpl implements RoleService {

	private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
	private final Rolerepository rolerepository;

	public RoleServiceImpl(Rolerepository rolerepository) {
		this.rolerepository = rolerepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Role findOneByRoleType(RoleType roleType) {
		log.info("[RoleService/FindOneByRoleType]:: Execution started.");

		try {
			Role data = this.rolerepository
				.findByRole(roleType)
				.orElseThrow(
					() -> new RoleNotFoundException("Role with TYPE " + roleType + " not found")
				);

			log.info("[RoleServiceImpl/findOneByRoleType]:: User found. User ID: {}", roleType);

			return data;
		} catch (UserNotFoundException ex) {
			log.error(
				"[RoleServiceImpl/findOneByRoleType]:: Exception occurred while retrieving role. Role TYPE : {}. Exception: {}",
				roleType,
				ex.getMessage()
			);
			throw ex;
		} catch (RuntimeException ex) {
			log.error(
				"[RoleServiceImpl/findOneByRoleType]:: Exception occurred while retrieving role from database , Exception message {}",
				ex.getMessage()
			);
			throw new RoleServiceException("Failed to retrieve user due to an unexpected error");
		} finally {
			log.info("[RoleServiceImpl/findOneByRoleType]:: Execution ended.");
		}
	}
}
