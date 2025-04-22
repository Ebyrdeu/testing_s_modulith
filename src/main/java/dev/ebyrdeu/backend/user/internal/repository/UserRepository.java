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
 * Repository interface for managing {@link User} entities.
 * <p>
 * This interface extends {@link JpaRepository} and provides additional query methods
 * for retrieving user data and related information using native SQL queries.
 * </p>
 * <p>
 * The following custom query methods are available:
 * <ul>
 *   <li>{@link #findOneByEmail(String)}: Retrieve a user by their email address.</li>
 *   <li>{@link #findAllWithMinimalInfo()}: Retrieve all users with minimal information.</li>
 *   <li>{@link #findOneByIdWithMinimalInfo(Long)}: Retrieve minimal information for a user by their ID.</li>
 *   <li>{@link #findOneByEmailWithMinimalInfo(String)}: Retrieve minimal information for a user by their email address.</li>
 *   <li>{@link #findRolesByEmail(String)}: Retrieve the roles associated with a user by their email address.</li>
 *   <li>{@link #addSingleRole(Long, Long)}: Associate a role with a user by inserting a record into the user_role table.</li>
 * </ul>
 * </p>
 *
 * @author Maxim Khnykin
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	/**
	 * Retrieves a {@link User} entity by its email address.
	 *
	 * @param email the email address of the user.
	 * @return an {@link Optional} containing the found {@link User}, or an empty {@link Optional} if no user is found.
	 */
	@Query(
		value = "select * from users u where u.email = :email",
		nativeQuery = true
	)
	Optional<User> findOneByEmail(@Param("email") String email);

	/**
	 * Retrieves a list of users with minimal information.
	 * <p>
	 * The query selects only the username, first name, and last name for each user.
	 * </p>
	 *
	 * @return a {@link List} of {@link UserMinimalInfoProjection} objects containing minimal user details.
	 */
	@Query(
		value = "select u.username as username, u.first_name as firstName, u.last_name as lastName from users u",
		nativeQuery = true
	)
	List<UserMinimalInfoProjection> findAllWithMinimalInfo();

	/**
	 * Retrieves minimal user information for a specific user identified by their ID.
	 *
	 * @param username the unique identifier of the user.
	 * @return an {@link Optional} containing a {@link UserMinimalInfoProjection} with minimal information
	 * for the specified user, or an empty {@link Optional} if the user is not found.
	 */
	@Query(
		value = "select u.username as username, u.first_name as firstName, u.last_name as lastName from users u where u.username = :username",
		nativeQuery = true
	)
	Optional<UserMinimalInfoProjection> findOneByUsernameWithMinimalInfo(@Param("username") String username);

	/**
	 * Retrieves minimal user information for a specific user identified by their email address.
	 *
	 * @param email the email address of the user.
	 * @return an {@link Optional} containing a {@link UserMinimalInfoProjection} with minimal information
	 * for the specified user, or an empty {@link Optional} if the user is not found.
	 */
	@Query(
		value = "select username as username, u.first_name as firstName, u.last_name as lastName from users u where u.email = :email",
		nativeQuery = true
	)
	Optional<UserMinimalInfoProjection> findOneByEmailWithMinimalInfo(@Param("email") String email);

	/**
	 * Retrieves a list of roles associated with a user identified by their email address.
	 * <p>
	 * This query performs a join between the {@code roles}, {@code user_role}, and {@code users} tables.
	 * It returns the role names associated with the user.
	 * </p>
	 *
	 * @param email the email address of the user.
	 * @return a {@link List} of role names (as {@link String}) associated with the user.
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
	 * Associates a single role with a user by inserting a record into the {@code user_role} table.
	 * <p>
	 * This method uses a native SQL query to link a user with a role.
	 * </p>
	 *
	 * @param userId the ID of the user.
	 * @param roleId the ID of the role to be associated with the user.
	 */
	@Modifying
	@Query(
		value = "insert into user_role (user_id, role_id) values (:userId, :roleId)",
		nativeQuery = true
	)
	void addSingleRole(@Param("userId") Long userId, @Param("roleId") Long roleId);


}
