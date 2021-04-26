package ase.txhistory.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.minidev.json.JSONObject;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long txid;
	private Long customerId;
	private Long vendorId;
	private final Date date;
	private double price;
	private String itemName;
	private int quantity;

	public Transaction() {
		this.date = new Date();
	}

	public Transaction(Long customerId, Long vendorId, double price, String itemName, int quantity) {
		this.customerId = customerId;
		this.vendorId = vendorId;
		this.price = price;
		this.itemName = itemName;
		this.quantity = quantity;
		this.date = new Date();
	}

	public JSONObject toJsonObject() {
		JSONObject j = new JSONObject();
		j.put("customerid", customerId);
		j.put("vendorid", vendorId);
		j.put("price", price);
		j.put("itemname", itemName);
		j.put("quantity", quantity);
		j.put("date", new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(date));
		return j;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[TxID: " + txid);
		sb.append("; Customer: " + customerId);
		sb.append("; Vendor: " + vendorId);
		sb.append(" |" + itemName + ", price: " + price + "€, quantity: " + quantity + "|]");
		return sb.toString();
	}

	public Long getTxid() {
		return txid;
	}

	public void setTxid(Long txid) {
		this.txid = txid;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getDate() {
		return date;
	}

}
