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
	private Long buyerId;
	private Long sellerId;
	private final Date date;
	private double price;
	private String itemTitle;
	private int count;

	public Transaction() {
		this.date = new Date();
	}

	public Transaction(Long buyerId, Long sellerId, double price, String itemTitle, int count) {
		this.buyerId = buyerId;
		this.sellerId = sellerId;
		this.price = price;
		this.itemTitle = itemTitle;
		this.count = count;
		this.date = new Date();
	};

	public JSONObject toJsonObject() {
		JSONObject j = new JSONObject();
		j.put("buyerid", buyerId);
		j.put("sellerid", sellerId);
		j.put("price", price);
		j.put("itemtitle", itemTitle);
		j.put("count", count);
		j.put("date", new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(date));
		return j;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[TxID: " + txid);
		sb.append("; Buyer: " + buyerId);
		sb.append("; Seller: " + sellerId);
		sb.append(" |" + itemTitle + ", price: " + price + "€, count: " + count + "|]");
		return sb.toString();
	}

	public Long getTxid() {
		return txid;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public Long getSellerId() {
		return sellerId;
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

	public void setTxid(Long txid) {
		this.txid = txid;
	}
}
