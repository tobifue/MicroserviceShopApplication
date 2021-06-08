package com.tobi.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Item class with all constructors to manage retrieved list of items
 * when calling txhistory endpoint in AccountService.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Long txid;
    private Long customerId;
    private Long vendorId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    private double price;
    private String itemTitle;
    private int quantity;
}



