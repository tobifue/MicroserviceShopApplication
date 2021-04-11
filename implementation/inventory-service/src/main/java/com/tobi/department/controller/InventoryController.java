package com.tobi.department.controller;

import com.tobi.department.entity.Inventory;
import com.tobi.department.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
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
    public Inventory saveDepartment(@RequestBody Inventory inventory){

        log.info("Inside saceDepartment method of DepartmentController");
        return inventoryService.saveItem(inventory);
    }

    //get Item
    @GetMapping("/{id}")
    public Inventory findByItemId(@PathVariable("id")Long itemId){
        log.info("Inside findDepartmentById method of DepartmentController");
        return inventoryService.findByItemId(itemId);
    }

    //get List of all Items from Vendor
    @RequestMapping(value="/vendor/{id}")
    @GetMapping
    public @ResponseBody List<Inventory> findAllObjects(@PathVariable("id")Integer id) {

        List<Inventory> inventories = new ArrayList<Inventory>();
        List<Inventory> v_inventories = new ArrayList<Inventory>();

        inventories = inventoryService.findByVendorId();

        for(Inventory dep : inventories) {
            if(dep.getVendorId()==id){
                v_inventories.add(dep);
            }
            else{
                //nothing to do here
            }
        }

        return v_inventories;
    }

    //update Item
    @RequestMapping(value = "/update/{id}")
    @PutMapping
    public Inventory update(@PathVariable("id") Long departmentId, @RequestBody Inventory dep) {
        Inventory inventory = inventoryService.findByItemId(departmentId);
        inventory.setPrice(dep.getPrice());
        inventory.setVendorId(dep.getVendorId());
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
