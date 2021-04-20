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
	private Long buyerId;
	private Long sellerId;
	private String email;
	private final Date date;
	private double price;
	private String itemTitle;
	private int count;
	private String shippingStatus; // not an enum for easy serialization

	public Shipment() {
		this.date = new Date();
		this.shippingStatus = ShippingStatus.ORDER_RECEIVED.getTitle();
	}

	public Shipment(Long buyerId, Long sellerId, Long itemid, String email, double price, String itemTitle, int count) {
		this.buyerId = buyerId;
		this.sellerId = sellerId;
		this.itemid = itemid;
		this.email = email;
		this.price = price;
		this.itemTitle = itemTitle;
		this.shippingStatus = ShippingStatus.ORDER_RECEIVED.getTitle();
		this.count = count;
		this.date = new Date();
	};

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[Shipment: " + shipmentid);
		sb.append("; Buyer: " + buyerId);
		sb.append("; Seller: " + sellerId);
		sb.append(" |" + itemTitle + ", price: " + price + "€, count: " + count + "|");
		sb.append(" status: " + shippingStatus);
		sb.append("; email: " + email);
		return sb.toString();
	}

	public JSONObject toJsonObject() {
		JSONObject j = new JSONObject();
		j.put("shipmentid", shipmentid);
		j.put("itemid", itemid);
		j.put("buyerid", buyerId);
		j.put("sellerid", sellerId);
		j.put("email", email);
		j.put("price", price);
		j.put("itemtitle", itemTitle);
		j.put("count", count);
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

	public Long getBuyerId() {
		return buyerId;
	}

	public Long getSellerId() {
		return sellerId;
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

	public String getItemTitle() {
		return itemTitle;
	}

	public int getCount() {
		return count;
	}

	public String getShippingStatus() {
		return shippingStatus;
	}

	// necessary for reflection, otherwise final

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public void setShipmentId(Long shipmentid) {
		this.shipmentid = shipmentid;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

}
