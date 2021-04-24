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
	private Long customerid;
	private Long itemId;
	private String itemName;
	private double rating;

	public Rating() {
	}

	public Rating(Long customerid, Long itemId, String itemName, double rating) {
		this.customerid = customerid;
		this.itemId = itemId;
		this.itemName = itemName;
		this.rating = rating;
	};

	public JSONObject toJsonObject() {
		JSONObject j = new JSONObject();
		j.put("customerid", customerid);
		j.put("itemId", itemId);
		j.put("itemName", itemName);
		j.put("rating", rating);
		return j;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[itemId: " + itemId);
		sb.append("; itemName: " + itemName);
		sb.append("[rtnID: " + rtnId);
		sb.append("; ItemName: " + itemName);
		sb.append("; itemId: " + itemId);
		sb.append("; Rating: " + rating);
		sb.append("]");
		return sb.toString();
	}

	public Long getRtnId() {
		return rtnId;
	}

	public Long getItemId() {
		return itemId;
	}

	public Long getcustomerid() {
		return customerid;
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
