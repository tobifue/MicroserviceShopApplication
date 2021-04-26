package ase.shipment.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.minidev.json.JSONObject;

@Entity
public class Shipment {

	public enum ShippingStatus {
		ORDER_RECEIVED("order received"),

		IN_DISTR_CENTER("in distribution center"),

		IN_DELIVERY("in delivery"),

		DELIVERED("delivered");

		private String title;

		ShippingStatus(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}

	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long shipmentid;
	private Long itemid;
	private Long customerId;
	private Long vendorId;
	private String email;
	private final Date date;
	private double price;
	private String itemName;
	private int quantity;
	private String shippingStatus; // not an enum for easy serialization

	public Shipment() {
		this.date = new Date();
		this.shippingStatus = ShippingStatus.ORDER_RECEIVED.getTitle();
	}

	public Shipment(Long customerId, Long vendorId, Long itemid, String email, double price, String itemName,
			int quantity) {
		this.customerId = customerId;
		this.vendorId = vendorId;
		this.itemid = itemid;
		this.email = email;
		this.price = price;
		this.itemName = itemName;
		this.shippingStatus = ShippingStatus.ORDER_RECEIVED.getTitle();
		this.quantity = quantity;
		this.date = new Date();
	};

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[Shipment: " + shipmentid);
		sb.append("; Customer: " + customerId);
		sb.append("; Vendor: " + vendorId);
		sb.append(" |" + itemName + ", price: " + price + "€, quantity: " + quantity + "|");
		sb.append(" status: " + shippingStatus);
		sb.append("; email: " + email);
		return sb.toString();
	}

	public JSONObject toJsonObject() {
		JSONObject j = new JSONObject();
		j.put("shipmentid", shipmentid);
		j.put("itemid", itemid);
		j.put("customerid", customerId);
		j.put("vendorid", vendorId);
		j.put("email", email);
		j.put("price", price);
		j.put("itemname", itemName);
		j.put("quantity", quantity);
		j.put("shippingstatus", shippingStatus);
		j.put("date", new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(date));
		return j;
	}

	public void updateShippingStatus() {
		switch (shippingStatus) {
		case "order received":
			setShippingStatus(ShippingStatus.IN_DISTR_CENTER.getTitle());
			break;
		case "in distribution center":
			setShippingStatus(ShippingStatus.IN_DELIVERY.getTitle());
			break;
		case "in delivery":
		default:
			setShippingStatus(ShippingStatus.DELIVERED.getTitle());
			break;
		}
	}

	public Long getShipmentId() {
		return shipmentid;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public Long getItemId() {
		return itemid;
	}

	public String getEmail() {
		return email;
	}

	public Date getDate() {
		return date;
	}

	public double getPrice() {
		return price;
	}

	public String getItemName() {
		return itemName;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getShippingStatus() {
		return shippingStatus;
	}

	public void setShipmentid(Long shipmentid) {
		this.shipmentid = shipmentid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

}
