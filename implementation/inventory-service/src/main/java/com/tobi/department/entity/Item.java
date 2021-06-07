package com.tobi.department.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Item{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemId;
    private Double price;
    private Integer vendorId;
    private Integer quantity;
    public String itemName;
    public Double priceRecommendation;

    public Item(Double price, Integer vendorId, Integer quantity, String itemName, Double priceRecommendation) {
        this.price = price;
        this.vendorId = vendorId;
        this.quantity = quantity;
        this.itemName = itemName;
        this.priceRecommendation = priceRecommendation;
    }

    public Item(Long itemId, Double price, Integer vendorId, Integer quantity, String itemName, Double priceRecommendation) {
        this.itemId = itemId;
        this.price = price;
        this.vendorId = vendorId;
        this.quantity = quantity;
        this.itemName = itemName;
        this.priceRecommendation = priceRecommendation;
    }

    public Item() {
    }

    public Long getItemId() {
        return itemId;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public Double getPriceRecommendation() {
        return priceRecommendation;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPriceRecommendation(Double priceRecommendation) {
        this.priceRecommendation = priceRecommendation;
    }

}

