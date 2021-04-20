package ase.gateway.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import ase.gateway.util.AdressUtil;
import ase.gateway.util.NetworkUtil;

@RestController
@RequestMapping("cart")
public class CartController {
	private static final String serviceName = "cart-service";

	/**
	 * @param item in format { ... }
	 * @return the transaction
	 */
	@PostMapping(path = "/addItemToCart/{costumerId}", consumes = "application/json", produces = "application/json")
	public String addItemToCart(@RequestBody Map<String, Object> item, @PathVariable("costumerId") Long costumerId) {
		System.out.println("cart Controller is called with" + item.toString());
		try {
			return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "/addItem/" + costumerId, item);
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@PostMapping(path = "/getCart/{costumerId}", consumes = "application/json", produces = "application/json")
	public String getCart(@RequestBody Map<String, Object> item, @PathVariable("costumerId") Long costumerId) {
		System.out.println("getCart is called with" + item.toString());
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), "/getCart/" + costumerId);
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
