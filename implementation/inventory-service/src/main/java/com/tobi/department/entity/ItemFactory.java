package com.tobi.department.entity;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tobi.department.repository.InventoryRepository;
import net.minidev.json.JSONArray;
import org.json.JSONException;

/**
 * Factory pattern for items.
 */
public class ItemFactory {

    /**
     * Creates Item.
     * @param itemId,price,vendorId,quantity,itemName,priceRecommendation
     * @return created Item
     */
    public static Item createInstance(Long itemId, Double price, Integer vendorId, Integer quantity, String itemName, Double priceRecommendation) {
        if (itemId != 0) {
            return new Item(itemId, price, vendorId, quantity, itemName, priceRecommendation);
        } else {
            return new Item(price, vendorId, quantity, itemName, priceRecommendation);
        }
    }
}