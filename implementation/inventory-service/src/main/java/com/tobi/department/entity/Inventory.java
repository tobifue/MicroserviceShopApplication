package com.tobi.department.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemId;
    private Double price;
    private Integer vendorId;
    private Integer quantity;
    public String itemName;
    public Double priceRecommendation;

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

