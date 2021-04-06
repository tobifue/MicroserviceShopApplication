package ase.checkout.itemHandler;

public class Item {
    private String name;
    private double price;
    private String vendorId;
    private int quantity;

    public Item() {
    }

    public Item(String name, double price, String vendorId, int quantity) {
        this.name = name;
        this.price = price;
        this.vendorId = vendorId;
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getVendorId() {
        return vendorId;
    }

    public int getQuantity() {
        return quantity;
    }

}
