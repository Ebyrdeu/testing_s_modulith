package dev.ebyrdeu.backend.user.internal.infrastructure.projection;

/**
 * Shows {@code username} {@code first name} {@code last name}
 *
 * @author Maxim Khnykin
 * @version 1.0
 */
public interface UserMinimalInfoProjection {
	String getUsername();

	String getFirstName();

	String getLastName();
}
