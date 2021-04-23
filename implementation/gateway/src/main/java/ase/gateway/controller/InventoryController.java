package ase.gateway.controller;

import java.io.IOException;
import java.util.Map;

import ase.gateway.traffic.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import ase.gateway.util.AdressUtil;
import ase.gateway.util.NetworkUtil;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	private static final String serviceName = "inventory-service";

	@PostMapping("/")
	public String saveItem(@RequestBody Map<String, Object> inventory) {
		try {
			return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "/", inventory);
		} catch (RestClientException | IOException e) {
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