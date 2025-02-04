package dev.ebyrdeu.backend.role.internal.infrastructure.repository;

import dev.ebyrdeu.backend.role.internal.infrastructure.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Rolerepository extends JpaRepository<Role, Long> {

}
