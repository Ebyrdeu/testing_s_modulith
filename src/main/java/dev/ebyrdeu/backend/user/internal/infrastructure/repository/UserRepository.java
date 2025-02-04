package dev.ebyrdeu.backend.user.internal.infrastructure.repository;

import dev.ebyrdeu.backend.user.internal.infrastructure.entity.User;
import dev.ebyrdeu.backend.user.internal.infrastructure.projection.UserMinimalInfoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Maxim Khnykin
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "select u.username as username, u.firstName as firstName, u.lastName as lastName from User u")
	List<UserMinimalInfoProjection> findAllWithMinimalInfo();

	@Query(value = "select u.username as username, u.firstName as firstName, u.lastName as lastName from User u where u.id = :id")
	Optional<UserMinimalInfoProjection> findOneByIdWithMinimalInfo(Long id);
}
