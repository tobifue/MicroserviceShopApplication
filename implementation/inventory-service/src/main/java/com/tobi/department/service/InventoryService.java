package com.tobi.department.service;

import com.tobi.department.entity.Item;

import com.tobi.department.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public Item saveItem(Item inventory) {
        log.info("Inside saveItem of InventoryService");
        return inventoryRepository.save(inventory);
    }

    public Item findByItemId(Long itemId) {
        log.info("Inside findByItemId of InventoryService");
        return inventoryRepository.findByItemId(itemId);
    }

    public List<Item> findByVendorId() {
        log.info("Inside findByVendorId of InventoryService");
        return inventoryRepository.findAll();
    }

    public void deleteByItemId(Long itemId) {
        log.info("Inside deleteByItemId of InventoryService");
        inventoryRepository.deleteById(itemId);
    }

}
