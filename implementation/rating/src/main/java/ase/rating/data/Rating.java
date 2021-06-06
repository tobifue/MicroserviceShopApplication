package ase.rating.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.minidev.json.JSONObject;

@Entity
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long rtnId;
	private Long customerId;
	private String itemName;
	private double rating;

	public Rating() {
	}

	public Rating(Long customerId, Long itemId, String itemName, double rating) {
		this.customerId = customerId;
		this.itemName = itemName;
		this.rating = rating;
	};

	public JSONObject toJsonObject() {
		JSONObject j = new JSONObject();
		j.put("customerId", customerId);
		j.put("itemName", itemName);
		j.put("rating", rating);
		return j;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[rtnID: " + rtnId);
		sb.append("; customerId: " + customerId);
		sb.append("; itemName: " + itemName);
		sb.append("; Rating: " + rating);
		sb.append("]");
		return sb.toString();
	}

	public Long getRtnId() {
		return rtnId;
	}

	public Long getcustomerId() {
		return customerId;
	}

	public double getRating() {
		return rating;
	}

	public String getItemName() {
		return itemName;
	}

	public void setRtnId(Long rtnId) {
		this.rtnId = rtnId;
	}

}
