package dev.ebyrdeu.backend.role;

import dev.ebyrdeu.backend.role.internal.infrastructure.entity.Role;


public interface RoleService {
	Role findOneByRoleType(RoleType roleType);

}
