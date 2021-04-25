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
	private Long customerId;
	private Long vendorId;
	private double price;
	private String email;
	private String itemName;

	public MarkedProduct(Long customerId, Long vendorId, double price, String email, String itemName) {
		this.buyerId = customerId;
		this.sellerId = vendorId;
		this.price = price;
		this.email = email;
		this.itemTitle = itemName;
	};

	public JSONObject toJsonObject() {
		JSONObject j = new JSONObject();
		j.put("customerId", customerId);
		j.put("vendorId", vendorId);
		j.put("price", price);
		j.put("itemName", itemName);
		return j;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[itemid: " + id);
		sb.append("; Customer: " + customerId);
		sb.append("; Vendor: " + vendorId);
		sb.append(" |" + itemName + ", price: " + price + "€|]");
		return sb.toString();
	}

	public Long getId() {
		return id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public double getPrice() {
		return price;
	}

	public String getEmail() {
		return email;
	}

	public String getItemName() {
		return itemName;
	}

	// necessary for reflection, otherwise final

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

}
