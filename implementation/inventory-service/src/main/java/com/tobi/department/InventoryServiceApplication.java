package com.tobi.department;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tobi.department.entity.Item;
import com.tobi.department.entity.ItemFactory;
import com.tobi.department.repository.InventoryRepository;
import com.tobi.department.service.InventoryService;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

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

	@SneakyThrows
	@RequestMapping(value="/items/")
	@GetMapping(produces = "application/json")
	public @ResponseBody String findAllItems() throws JsonProcessingException {

		List<Item> itemss = new ArrayList<Item>();

		itemss = inventoryService.findAllItems();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(itemss);

		return json;
	}

        @SneakyThrows
		@RequestMapping(value="/vendor")
        @GetMapping(consumes="application/json", produces ="application/json")
        public @ResponseBody String findAllObjects() {

            List<Item> inventories = new ArrayList<Item>();

            inventories = inventoryService.findAllItems();

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(inventories);

            return json;
        }

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Autowired
	private RestTemplate restTemplate;

	//update Item
	@SneakyThrows
	@RequestMapping(value = "/update/{id}", consumes = "application/json", produces="application/json")
	@PostMapping
	public String update(@PathVariable("id") Long departmentId, @RequestBody Item update_item) throws JsonProcessingException {
		Item inventory = inventoryService.findByItemId(departmentId);

		if(update_item.getVendorId()!=null) {
			inventory.setVendorId(update_item.getVendorId());
		}
		if(update_item.getQuantity()!=null) {
			inventory.setQuantity(update_item.getQuantity());
		}
		if(update_item.getItemName()!=null) {
			inventory.setItemName(update_item.getItemName());
		}
		if(update_item.getPrice()!=null) {
			inventory.setPrice(update_item.getPrice());
		}
		//code
		HttpHeaders headers = new HttpHeaders();
// set `content-type` header
		headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		Map<String, Object> map = new HashMap<>();
		map.put("itemName", inventory.getItemName());
		map.put("vendorId", inventory.getVendorId().toString());
		map.put("price", inventory.getPrice().toString());

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8090/update", entity, String.class);

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

	@RequestMapping(value = "/registerWithGateway", method = RequestMethod.GET)
	private boolean registerWithGateway() {
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
					add("/items/");
				}
			});
			String checkoutAdress = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port;
			String gatewayIp = "http://" + (System.getenv("GATEWAYIP") == null ? "localhost" : System.getenv("GATEWAYIP")) + ":8080";
			registrationDetails.put("category", "inventory");
			registrationDetails.put("ip", checkoutAdress);
			new RestTemplate().postForObject(String.format("%s/%s", gatewayIp, "/register/new"),
					registrationDetails, String.class);
			System.out.println("Successfully registered with gateway!");
			return true;
		} catch (RestClientException | UnknownHostException e) {
			System.err.println("Failed to connect to Gateway, please register manually or restart application");
			return false;
		}
	}

	@Bean
	public CommandLineRunner continuousRegistrationWithGateway() {
		return (args) -> {
			new Thread(() -> {
				while (!registerWithGateway()) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		};
	}

	@RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
	@ResponseBody
	public String heartbeat() {
		return "OK";
	}

	@Bean
	public CommandLineRunner registerWithGateWay() {
		return (args) -> {
			// register with gateway in commandlineRunner
			registerWithGateway();
		};
	}

}
