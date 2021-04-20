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
	private Long buyerId;
	private Long itemId;
	private String itemTitle;
	private Long rating;

	public Rating() {
	}

	public Rating(Long buyerId, Long itemId, String itemTitle, Long rating) {
		this.buyerId = buyerId;
		this.itemId = itemId;
		this.itemTitle = itemTitle;
		this.rating = rating;
	};

	public JSONObject toJsonObject() {
		JSONObject j = new JSONObject();
		j.put("buyerId", buyerId);
		j.put("itemId", itemId);
		j.put("itemTitle", itemTitle);
		j.put("rating", rating);
		return j;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[rtnID: " + rtnId);
		sb.append("; ItemTitle: " + itemTitle);
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

	public Long getBuyerId() {
		return buyerId;
	}

	public Long getRating() {
		return rating;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setRtnId(Long rtnId) {
		this.rtnId = rtnId;
	}

}
