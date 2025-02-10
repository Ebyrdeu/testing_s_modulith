/**
 * The use of {@code moduleName :: *} is strictly forbidden. Please use named interfaces to the explicitly defined
 * dependencies to maintain modular integrity.
 *
 * @see org.springframework.modulith.ApplicationModule
 */

@org.springframework.modulith.ApplicationModule(
	displayName = "Security",
	allowedDependencies = {
		"user",
	}
)
@org.springframework.lang.NonNullApi
package dev.ebyrdeu.backend.security;