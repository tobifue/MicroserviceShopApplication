package ase.gateway.user;

import javax.persistence.Entity;

@Entity
public class Admin extends User {

	@Override
	public String toString() {
		return "Admin; ID: " + id + "; Email: " + email;
	}
}
