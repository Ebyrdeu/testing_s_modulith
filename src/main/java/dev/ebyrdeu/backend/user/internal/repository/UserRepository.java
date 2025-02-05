package dev.ebyrdeu.backend.user.internal.repository;

import dev.ebyrdeu.backend.user.internal.model.User;
import dev.ebyrdeu.backend.user.internal.projection.UserMinimalInfoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Maxim Khnykin
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByEmail(String email);

	@Query(value = "select u.username as username, u.firstName as firstName, u.lastName as lastName from User u")
	List<UserMinimalInfoProjection> findAllWithMinimalInfo();

	@Query(value = "select u.username as username, u.firstName as firstName, u.lastName as lastName from User u where u.id = :id")
	Optional<UserMinimalInfoProjection> findOneByIdWithMinimalInfo(Long id);

	/**
	 * Retrieves a list of roles associated with a user by their email address.
	 * This method uses a native SQL query to join the `roles`, `user_role`, and `users` tables.
	 *
	 * @param email the email address of the user
	 * @return a list of role names (as strings) associated with the user
	 */
	@Query(
		value = """
				SELECT r.role FROM roles r\s
				JOIN user_role ur ON r.id = ur.role_id\s
				JOIN users u ON u.id = ur.user_id\s
				WHERE u.email = :email
			\t""",
		nativeQuery = true
	)
	List<String> findRolesByEmail(@Param("email") String email);

	/**
	 * Adds a single role to a user by associating the user ID with a role ID in the `user_role` table.
	 * This method uses a native SQL query to perform the insertion.
	 *
	 * @param userId the ID of the user to whom the role will be added
	 * @param roleId the ID of the role to be added to the user
	 */
	@Modifying
	@Query(value = "insert into user_role (user_id, role_id) values (:userId, :roleId)", nativeQuery = true)
	void addSingleRole(@Param("userId") Long userId, @Param("roleId") Long roleId);


}
