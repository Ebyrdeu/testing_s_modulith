package dev.ebyrdeu.backend.user.internal.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for updating user information.
 * <p>
 * This record is used as the request payload when patching a user's details, such as:
 * <ul>
 *   <li>{@code username}: the new username for the user; must be non-blank and 3–25 characters long.</li>
 *   <li>{@code aboutMe}: the updated 'about me' section; optional, maximum 150 characters.</li>
 * </ul>
 * </p>
 * <p>
 * Jackson's {@link JsonInclude} annotation ensures that only non-empty fields are serialized
 * into the JSON payload.
 * </p>
 * <p>
 * Example JSON:
 * <pre>
 * {
 *   "username": "newUsername",
 *   "aboutMe": "A short bio or description."
 * }
 * </pre>
 * </p>
 * <p>
 * Validation rules:
 * <ul>
 *   <li>{@code username}:
 *     <ul>
 *       <li>Cannot be blank (enforced by {@link NotBlank}).</li>
 *       <li>Must have a length between 3 and 25 characters (enforced by {@link Size}).</li>
 *     </ul>
 *   </li>
 *   <li>{@code aboutMe}:
 *     <ul>
 *       <li>Optional field (can be null or empty).</li>
 *       <li>Maximum length of 150 characters.</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * @param username the new username for the user
 * @param aboutMe  the updated 'about me' description for the user
 * @author Maxim Khnykin
 * @version 1.0
 * @see Serializable
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record UserInfoReqDto(

		@NotBlank(message = "Username cannot be Blank")
		@Size(min = 3, max = 25)
		String username,

		@Size(max = 150)
		String aboutMe

) implements Serializable {
}
