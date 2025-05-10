package dev.ebyrdeu.backend.user.internal.model;

import dev.ebyrdeu.backend.common.entity.DefaultEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * @author Maxim Khnykin
 * @version 1.0
 * @see DefaultEntity
 */
@Entity
@Table(name = "users")
public class User extends DefaultEntity {

	@Column(name = "username", length = 50, unique = true, nullable = false)
	private String username;

	@Column(name = "first_name", length = 50)
	private String firstName;

	@Column(name = "last_name", length = 50)
	private String lastName;

	@Column(name = "email", length = 100)
	private String email;

	@Column(name = "about_me", length = 150)
	private String aboutMe;

	public User() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}
}