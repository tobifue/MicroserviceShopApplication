package ase.gateway.controller;

import ase.gateway.traffic.Message;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	private static final String serviceName = "inventory-service";

	@PostMapping("/")
	public String saveItem(@RequestBody Map<String, Object> inventory) {
		try {
			//return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "/", inventory);
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(inventory, "inventory", String.format("/"), "GET"));

		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	// get Item
	@GetMapping("/{id}")
	public String findByItemId(@PathVariable("id") Long itemId) {
		try {
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "inventory", String.format("/%s", itemId), "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@GetMapping("/vendor/{id}")
	public @ResponseBody String findAllObjects(@PathVariable("id") Integer id) {
		try {
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "inventory", String.format("/vendor/%s", id), "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/vendor", method = RequestMethod.GET)
	@ResponseBody
	public String findAllObjects() {
		try {
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "inventory", String.format("/vendor"), "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/update/{id}")
	@PostMapping
	public String update(@PathVariable("id") Long departmentId, @RequestBody Map<String, Object> inventory) {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(inventory, "inventory", String.format("/update/%s", departmentId), "POST"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	// delete Item
	@RequestMapping(value = "/delete/{id}")
	@PostMapping
	public String delete(@PathVariable("id") Long departmentId) {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(null,"inventory",String.format("/delete/%s", departmentId), "POST"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}