package dev.ebyrdeu.backend.role.internal.service;

import dev.ebyrdeu.backend.role.RoleService;
import dev.ebyrdeu.backend.role.RoleType;
import dev.ebyrdeu.backend.role.internal.infrastructure.entity.Role;
import dev.ebyrdeu.backend.role.internal.infrastructure.repository.Rolerepository;
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

		return this.rolerepository.findByRole(roleType)
			.orElseThrow(
				() -> new RuntimeException("Not Role is Found")
			);
	}
}
