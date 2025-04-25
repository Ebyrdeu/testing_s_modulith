/**
 * The use of {@code moduleName :: *} is strictly forbidden. Please use named interfaces to the explicitly defined
 * dependencies to maintain modular integrity.
 *
 * @see org.springframework.modulith.ApplicationModule
 */
@org.springframework.modulith.ApplicationModule(
	displayName = "Image",
	allowedDependencies = {
		"common :: entity",
		"common :: dto",
		"common :: util",
	}
)
package dev.ebyrdeu.backend.image;