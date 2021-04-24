package ase.notification.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.minidev.json.JSONObject;

@Entity
public class Notification {

	public enum EmailBody {

		shipmentEmail("Your current shipment status: "),

		markedProductEmail("The price of your marked product has changed to: ");

		private String body;

		EmailBody(String body) {
			this.body = body;
		}

		public String getBody() {
			return body;
		}

	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long nid;
	private Long customerid;
	private Long itemId;
	private String itemName;
	private double price;
	private double newPrice;
	private String email;
	private String shippingStatus;
	private String emailBody;

	public Notification() {
		this.emailBody = EmailBody.shipmentEmail.getBody();
	}

	public Notification(Long customerid, Long itemId, double price, String itemName, double newPrice,
			String shippingStatus, String email) {
		this.customerid = customerid;
		this.itemId = itemId;
		this.itemName = itemName;
		this.price = price;
		this.newPrice = newPrice;
		this.shippingStatus = shippingStatus;
		this.email = email;
		this.emailBody = EmailBody.shipmentEmail.getBody();
	};

	public JSONObject toJsonObject() {
		JSONObject j = new JSONObject();
		j.put("customerid", customerid);
		j.put("itemId", itemId);
		j.put("price", price);
		j.put("itemName", itemName);
		j.put("shippingStatus", shippingStatus);
		j.put("email", email);
		return j;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("customer: " + customerid);
		sb.append("; Item: " + itemId);
		sb.append("; itemName: " + itemName);
		sb.append("; price: " + price);
		sb.append("; shippingStatus " + shippingStatus);
		sb.append("; email " + email);
		sb.append("]");
		return sb.toString();
	}

	public void updateEmailBody() {
		switch (emailBody) {
		case "Your current shipment status: ":
			setEmailBody(EmailBody.shipmentEmail.getBody());
			break;
		case "The price of your marked product has changed to: ":
			setEmailBody(EmailBody.markedProductEmail.getBody());
			break;
		default:
			setEmailBody(EmailBody.shipmentEmail.getBody());
			break;
		}
	}

	public Long getNid() {
		return nid;
	}

	public Long getcustomerid() {
		return customerid;
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

	public String getItemName() {
		return itemName;
	}

	public String getEmail() {
		return email;
	}

	public String getShippingStatus() {
		return shippingStatus;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

}
