package com.tobi.department;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tobi.department.entity.Inventory;
import com.tobi.department.service.InventoryService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Autowired
	private InventoryService inventoryService;

	//create Item
	@SneakyThrows
	@PostMapping(path ="/", consumes = "application/json", produces = "application/json")
	public String saveItem(@RequestBody Inventory inventory){

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(inventoryService.saveItem(inventory));

		return json;
	}

	//get Item
	@SneakyThrows
	@GetMapping(path = "/{id}", produces = "application/json")
	public String findByItemId(@PathVariable("id")Long itemId){

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(inventoryService.findByItemId(itemId));

		return json;
	}

	//get List of all Items from Vendor
	@SneakyThrows
	@RequestMapping(value="/vendor/{id}")
	@GetMapping(produces = "application/json")
	public @ResponseBody String findAllObjects(@PathVariable("id")Integer id) {

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

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(v_inventories);

		return json;
	}

        @SneakyThrows
		@RequestMapping(value="/vendor")
        @GetMapping(consumes="application/json", produces ="application/json")
        public @ResponseBody String findAllObjects() {

            List<Inventory> inventories = new ArrayList<Inventory>();

            inventories = inventoryService.findByVendorId();

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(inventories);

            return json;
        }

	//update Item
	@SneakyThrows
	@RequestMapping(value = "/update/{id}", consumes = "application/json", produces="application/json")
	@PostMapping
	public String update(@PathVariable("id") Long departmentId, @RequestBody Inventory dep) throws JsonProcessingException {
		Inventory inventory = inventoryService.findByItemId(departmentId);
		inventory.setPrice(dep.getPrice());
		inventory.setVendorId(dep.getVendorId());
		inventory.setQuantity(dep.getQuantity());
		//code

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(inventoryService.saveItem(inventory));

		return json;
	}

	//delete Item
	@RequestMapping(value = "/delete/{id}")
	@PostMapping
	public String delete(@PathVariable("id") Long departmentId) {

		inventoryService.deleteByItemId(departmentId);
		return "Delete successfull";
	}

}
