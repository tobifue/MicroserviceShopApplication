package com.tobi.department;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tobi.department.entity.Item;
import com.tobi.department.repository.InventoryRepository;
import com.tobi.department.service.InventoryService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public String saveItem(@RequestBody Item inventory){

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

		List<Item> inventories = new ArrayList<Item>();
		List<Item> v_inventories = new ArrayList<Item>();

		inventories = inventoryService.findByVendorId();

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

        @SneakyThrows
		@RequestMapping(value="/vendor")
        @GetMapping(consumes="application/json", produces ="application/json")
        public @ResponseBody String findAllObjects() {

            List<Item> inventories = new ArrayList<Item>();

            inventories = inventoryService.findByVendorId();

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(inventories);

            return json;
        }

	//update Item
	@SneakyThrows
	@RequestMapping(value = "/update/{id}", consumes = "application/json", produces="application/json")
	@PostMapping
	public String update(@PathVariable("id") Long departmentId, @RequestBody Item dep) throws JsonProcessingException {
		Item inventory = inventoryService.findByItemId(departmentId);
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
	@Value("${server.port}")
	private String port;

	private void registerWithGateway() {
		try {
			Map<String, Object> registrationDetails = new HashMap<>();
			registrationDetails.put("endpoints", new ArrayList<String>() {
				private static final long serialVersionUID = 1L;
				{
					// put highest level endpoints here
					add("/");
					add("/update");
					add("/delete");
					add("/vendor");
				}
			});
			registrationDetails.put("category", "inventory");
			registrationDetails.put("ip", "http://localhost:" + port);
			new RestTemplate().postForObject(String.format("%s/%s", "http://localhost:8080", "/register/new"),
					registrationDetails, String.class);
		} catch (RestClientException e) {
			System.out.println("Could not reach Gateway, retrying in 5 seconds");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			registerWithGateway();
		}
		System.out.println("Successfully registered with gateway!");
	}

	@Bean
	public CommandLineRunner registerWithGateWay() {
		return (args) -> {
			// register with gateway in commandlineRunner
			registerWithGateway();
		};
	}

}
