package com.tobi.department.service;

import com.tobi.department.entity.Item;

import com.tobi.department.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * InventoryService class manages domain logic.
 */
@Service
@Slf4j
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * Saves an item to repository
     * @param inventory of type Item
     * @return saved Item
     */
    public Item saveItem(Item inventory) {
        log.info("Inside saveItem of InventoryService");
        return inventoryRepository.save(inventory);
    }

    /**
     * Finds an item bei id.
     * @param itemId of type Long
     * @return found Item
     */
    public Item findByItemId(Long itemId) {
        log.info("Inside findByItemId of InventoryService");
        return inventoryRepository.findByItemId(itemId);
    }

    /**
     * Finds all items from repository
     * @return List of all Items
     */
    public List<Item> findAllItems() {
        log.info("Inside findByVendorId of InventoryService");
        return inventoryRepository.findAll();
    }

    /**
     * Deletes an item by id.
     * @param itemId of type Long
     */
    public void deleteByItemId(Long itemId) {
        log.info("Inside deleteByItemId of InventoryService");
        inventoryRepository.deleteById(itemId);
    }

}
