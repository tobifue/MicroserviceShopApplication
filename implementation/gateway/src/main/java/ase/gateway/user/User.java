package ase.gateway.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class User {

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long userId;
	protected String email;

	public User(String email, Long userId){
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
