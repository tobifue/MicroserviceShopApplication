package com.tobi.user.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Long itemId;
    private Integer price;
    private Integer vendorId;
    private Integer quantity;
    public String itemName;
}
