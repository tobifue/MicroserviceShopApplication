package com.tobi.department.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

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

/*
    public Long get_departmentId() {
        return departmentId;
    }

    public void set_departmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Integer get_price() {
        return price;
    }

    public void set_price(Integer price) {
        this.price = price;
    }

    public Integer get_vendorId(){
        return vendorId;
    }

    public void set_vendorId(Integer vendorId){
        this.vendorId=vendorId;
    }

 */
}

