package ase.gateway.user;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class User {

	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	protected Long userId;
	protected String email;

	public User(String email, Long userId) {
		this.email = email;
		this.userId = userId;
	}

	protected User() {
		this.email = userId + "@email.com"; // TODO add private email for testing
	};

	@Override
	public String toString() {
		return null;
	}

	public Long getId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
