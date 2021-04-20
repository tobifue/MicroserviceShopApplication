package ase.notification.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.minidev.json.JSONObject;

@Entity
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long nid;
	private Long buyerId;
	private Long itemId;
	private String itemTitle;
	private double price;
	private double newPrice;
	private String buyerEmail;

	public Notification() {
	}

	public Notification(Long buyerId, Long itemId, double price, String itemTitle, double newPrice, String buyerEmail) {
		this.buyerId = buyerId;
		this.itemId = itemId;
		this.itemTitle = itemTitle;
		this.price = price;
		this.itemTitle = itemTitle;
		this.newPrice = newPrice;
		this.buyerEmail = buyerEmail;
	};

	public JSONObject toJsonObject() {
		JSONObject j = new JSONObject();
		j.put("buyerid", buyerId);
		j.put("itemId", itemId);
		j.put("price", price);
		j.put("itemtitle", itemTitle);
		return j;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("; Buyer: " + buyerId);
		sb.append("; Item: " + itemId);
		sb.append("; itemTitle: " + itemTitle);
		sb.append("; itemTitle: " + itemTitle);
		sb.append("; price: " + price);
		sb.append("]");
		return sb.toString();
	}

	public Long getNid() {
		return nid;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public Long getItemId() {
		return itemId;
	}

	public double getNewPrice() {
		return newPrice;
	}

	public double getPrice() {
		return price;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public String getEmail() {
		return buyerEmail;
	}

}
