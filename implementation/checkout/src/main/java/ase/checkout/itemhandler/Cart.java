package ase.checkout.itemhandler;

import java.util.List;

public class Cart {
    public List<Item> items;
    private double price;
    private String costumerId;

    public Cart() {
    }
    

    public Cart(List<Item> items, double price, String costumerId) {
        this.items = items;
        this.price = price;
        this.costumerId = costumerId;
    }

    public double getPrice() {
        return price;
    }

    public String getCostumerId() {
        return costumerId;
    }
}
