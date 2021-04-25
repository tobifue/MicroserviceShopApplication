package ase.gateway.controller;

import ase.gateway.traffic.Message;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@RestController
@RequestMapping("cart")
public class CartController {
	//private static final String serviceName = "cart-service";


	/**
	 * @param item in format { ... }
	 * @return the transaction
	 */
	@PostMapping(path = "/addItemToCart/{customerId}", consumes = "application/json", produces = "application/json")
	public String addItemToCart(@RequestBody Map<String, Object> item, @PathVariable("customerId") Long customerId) {
		System.out.println("cart Controller is called with" + item.toString());
		try {
			//return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), String.format("/addItem/%s", customerId), item);
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(item, "cart", String.format("/addItem/%s", customerId), "POST"));

		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}


	@PostMapping(path = "/getCart/{customerId}", consumes = "application/json", produces = "application/json")
	public String getCart(@RequestBody Map<String, Object> item, @PathVariable("customerId") Long customerId) {
		System.out.println("getCart is called with" + item.toString());
		try {
			//return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), "/getCart/" + customerId);
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "cart", String.format("/getCart/%s", customerId), "GET"));

		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
