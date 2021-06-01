package ase.gateway.user;

import javax.persistence.Entity;

@Entity
public class Vendor extends User {

	@Override
	public String toString() {
		return "Vendor; ID: " + userId + "; Email: " + email;
	}
}
