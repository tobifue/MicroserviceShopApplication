package ase.markedproduct.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.minidev.json.JSONObject;

@Entity
public class MarkedProduct {

	public MarkedProduct() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long buyerId;
	private Long sellerId;
	private double price;
	private String email;
	private String itemTitle;

	public MarkedProduct(Long buyerId, Long sellerId, double price, String email, String itemTitle) {
		this.buyerId = buyerId;
		this.sellerId = sellerId;
		this.price = price;
		this.email = email;
		this.itemTitle = itemTitle;
	};

	public JSONObject toJsonObject() {
		JSONObject j = new JSONObject();
		j.put("buyerid", buyerId);
		j.put("sellerid", sellerId);
		j.put("price", price);
		j.put("itemtitle", itemTitle);
		return j;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[itemid: " + id);
		sb.append("; Buyer: " + buyerId);
		sb.append("; Seller: " + sellerId);
		sb.append(" |" + itemTitle + ", price: " + price + "€|]");
		return sb.toString();
	}

	public Long getId() {
		return id;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public double getPrice() {
		return price;
	}

	public String getEmail() {
		return email;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	// necessary for reflection, otherwise final

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

}
