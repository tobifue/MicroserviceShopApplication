
package com.tobi.department.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tobi.department.entity.Item;
import com.tobi.department.entity.ItemFactory;
import com.tobi.department.repository.InventoryRepository;
import com.tobi.department.service.InventoryService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;


    //create Item
    @PostMapping("/")
    public Item saveDepartment(@RequestBody Item inventory){

        Item newItem = ItemFactory.createInstance(inventory.getItemId(), inventory.getPrice(),inventory.getVendorId(), inventory.getQuantity(), inventory.getItemName(), inventory.getPriceRecommendation());
        log.info("Inside saveDepartment method of DepartmentController");
        return inventoryService.saveItem(newItem);
    }


    //get Item
    @GetMapping("/{id}")
    public Item findByItemId(@PathVariable("id")Long itemId){
        log.info("Inside findDepartmentById method of DepartmentController");
        return inventoryService.findByItemId(itemId);
    }

    //get all items
    @SneakyThrows
    @RequestMapping(value="/items/{id}")
    @GetMapping(produces = "application/json")
    public @ResponseBody String findAllItems(@PathVariable("id")Integer id) throws JsonProcessingException {

        List<Item> itemss = new ArrayList<Item>();

        log.info("Inside findDepartmentById method of DepartmentController");
        itemss = inventoryService.findAllItems();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(itemss);

        return json;
    }

    //get List of all Items from Vendor
    @SneakyThrows
    @RequestMapping(value="/vendor/{id}")
    @GetMapping(produces = "application/json")
    public @ResponseBody String findAllObjects(@PathVariable("id")Integer id) {

        List<Item> inventories = new ArrayList<Item>();
        List<Item> v_inventories = new ArrayList<Item>();

        inventories = inventoryService.findAllItems();

        for(Item dep : inventories) {
            if(dep.getVendorId()==id){
                v_inventories.add(dep);
            }
            else{
                //nothing to do here
            }
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(v_inventories);

        return json;
    }


    //update Item
    @RequestMapping(value = "/update/{id}")
    @PutMapping
    public Item update(@PathVariable("id") Long departmentId, @RequestBody Item dep) {
        Item inventory = inventoryService.findByItemId(departmentId);
        inventory.setPrice(dep.getPrice());
        inventory.setVendorId(dep.getVendorId());
        inventory.setQuantity(dep.getQuantity());
        //code

        return inventoryService.saveItem(inventory);
    }

    //delete Item
    @RequestMapping(value = "/delete/{id}")
    @PostMapping
    public String delete(@PathVariable("id") Long departmentId) {

        inventoryService.deleteByItemId(departmentId);
        return "Delete successfull";
    }
}
