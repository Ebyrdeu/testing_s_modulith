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
 * Repository interface for managing {@link User} entities and fetching
 * user data in various shapes (full entity, minimal projection or JSON payload).
 * <p>
 * Extends {@link JpaRepository} to inherit standard CRUD-operations.
 * Adds native queries for:
 * <ul>
 *   <li>Retrieving a full {@link User} by email.</li>
 *   <li>Fetching minimal user details (username, firstName, lastName) via a projection.</li>
 *   <li>Producing a JSON string payload containing user fields + associated images.</li>
 *   <li>Retrieving user roles.</li>
 *   <li>Linking a user to a role (inserting into {@code user_role}).</li>
 * </ul>
 * <p>
 * Note: The JSON-generating query (PostgreSQL) uses {@code json_build_object},
 * {@code json_agg} and returns a single-column JSON string named {@code user_with_images}.
 * </p>
 *
 * @author Maxim Khnykin
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find a {@link User} entity by its email address.
	 *
	 * @param email the email address to search for (non-null, unique).
	 * @return an {@link Optional} containing the matching {@link User}, or empty if none found.
	 */
	@Query(
		value = "select * from users u where u.email = :email",
		nativeQuery = true
	)
	Optional<User> findOneByEmail(@Param("email") String email);

	/**
	 * Retrieve all users with minimal information.
	 * <p>
	 * Returns only username, firstName and lastName wrapped in a
	 * {@link UserMinimalInfoProjection}.
	 * </p>
	 *
	 * @return a {@link List} of projections, never null (empty list if no users).
	 */
	@Query(
		value = """
			select
			    u.username    as username,
			    u.first_name  as firstName,
			    u.last_name   as lastName
			from users u
			""",
		nativeQuery = true
	)
	List<UserMinimalInfoProjection> findAllWithMinimalInfo();

	/**
	 * Retrieve minimal info for a single user, together with their images, as a JSON string.
	 * <p>
	 * The JSON object has the structure:
	 * <pre>
	 * {
	 *   "username":   "...",
	 *   "firstName":  "...",
	 *   "lastName":   "...",
	 *   "email":      "...",
	 *   "images": [
	 *     {
	 *       "title":       "...",
	 *       "description": "...",
	 *       "price":       0.00,
	 *       "imageUrl":    "..."
	 *     },
	 *     ...
	 *   ]
	 * }
	 * </pre>
	 * An empty array is returned if the user has no images.
	 * <p>
	 * Uses PostgreSQL’s {@code json_build_object}, {@code json_agg} and {@code FILTER}
	 * to assemble the payload.
	 * </p>
	 *
	 * @param username the unique username of the user (non-null).
	 * @return an {@link Optional} containing the JSON payload as {@link String},
	 * or empty if no user with the given username exists.
	 */
	@Query(
		value = """
			select json_build_object(
			         'username', u.username,
			         'firstName', u.first_name,
			         'lastName', u.last_name,
			         'email', u.email,
			         'images', coalesce(
			                      json_agg(
			                        json_build_object(
			                          'title',       i.title,
			                          'description', i.description,
			                          'price',       i.price,
			                          'imageUrl',    i.watermarked_image_url
			                        )
			                      ) filter (where i.id is not null),
			                      '[]'::json
			                    )
			       ) as user_with_images
			from users u
			left join images i
			  on u.id = i.user_id
			where u.username = :username
			group by
			  u.username,
			  u.first_name,
			  u.last_name,
			  u.email
			""",
		nativeQuery = true
	)
	Optional<String> findOneByUsernameWithImages(@Param("username") String username);

	/**
	 * Retrieve minimal info for a single user by email.
	 * <p>
	 * Returns a {@link UserMinimalInfoProjection} containing username,
	 * firstName and lastName for the matching user.
	 * </p>
	 *
	 * @param email the email address to search for (non-null).
	 * @return an {@link Optional} containing the projection, or empty if no match.
	 */
	@Query(
		value = """
			select
			    u.username    as username,
			    u.first_name  as firstName,
			    u.last_name   as lastName
			from users u
			where u.email = :email
			""",
		nativeQuery = true
	)
	Optional<UserMinimalInfoProjection> findOneByEmailWithMinimalInfo(@Param("email") String email);

	/**
	 * Retrieve all role names associated with a user identified by email.
	 * <p>
	 * Joins {@code users} → {@code user_role} → {@code roles}.
	 * </p>
	 *
	 * @param email the email address of the user (non-null).
	 * @return a {@link List} of role names (e.g. "ROLE_ADMIN"), empty if none.
	 */
	@Query(
		value = """
			select r.role
			from roles r
			join user_role ur on r.id = ur.role_id
			join users u     on u.id = ur.user_id
			where u.email = :email
			""",
		nativeQuery = true
	)
	List<String> findRolesByEmail(@Param("email") String email);

	/**
	 * Assign a single role to a user by inserting into {@code user_role}.
	 * <p>
	 * Executes within a transaction; caller must handle transaction boundaries.
	 * </p>
	 *
	 * @param userId the ID of the user (non-null).
	 * @param roleId the ID of the role to assign (non-null).
	 */
	@Modifying
	@Query(
		value = """
			insert into user_role (user_id, role_id)
			values (:userId, :roleId)
			""",
		nativeQuery = true
	)
	void addSingleRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
