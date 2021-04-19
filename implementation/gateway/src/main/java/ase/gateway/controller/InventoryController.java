package ase.gateway.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import ase.gateway.util.AdressUtil;
import ase.gateway.util.NetworkUtil;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	private static final String serviceName = "inventory-service";


	@PostMapping("/")
	public String saveItem(@RequestBody Map<String, Object> inventory){
		try {
			return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "/", inventory);
		} catch (RestClientException | IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	//get Item
	@GetMapping("/{id}")
	public String findByItemId(@PathVariable("id")Long itemId){
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), String.format("%d", itemId));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@GetMapping("/vendor/{id}")
	public @ResponseBody String findAllObjects(@PathVariable("id")Integer id) {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), String.format("vendor/%s", id));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/vendor", method = RequestMethod.GET)
	@ResponseBody
	public String findAllObjects() {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), "vendor");
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}