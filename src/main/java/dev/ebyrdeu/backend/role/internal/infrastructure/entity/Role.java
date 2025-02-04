package dev.ebyrdeu.backend.role.internal.infrastructure.entity;

import dev.ebyrdeu.backend.common.entity.DefaultEntity;
import dev.ebyrdeu.backend.role.RoleType;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;


@Entity
@Table(name = "roles")
public class Role extends DefaultEntity {


	@ColumnDefault("'USER'")
	@Enumerated(EnumType.STRING)
	@Column(name = "role", length = 10)
	private RoleType role;


	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

}