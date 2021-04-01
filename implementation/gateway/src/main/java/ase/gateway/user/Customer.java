package ase.gateway.user;

import javax.persistence.Entity;

@Entity
public class Customer extends User {

	@Override
	public String toString() {
		return "Customer; ID: " + id + "; Email: " + email;
	}

}
