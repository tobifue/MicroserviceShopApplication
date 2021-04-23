package ase.gateway.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import ase.gateway.traffic.Message;

@RestController
@RequestMapping("markedproduct")
public class MarkedProductController {

	private static String serviceName = "markedproduct";

	@RequestMapping(value = "/show/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String showAllProductsForUser(@PathVariable Long userid) {
		try {

			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(null, "markedproduct", "/show", "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/showall", method = RequestMethod.GET)
	@ResponseBody
	public String showAllMarkedProducts() {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(null, "markedproduct", "/showall", "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * @param message contains information about the update with fields "sellerid",
	 *                "itemTitle", "price"
	 * 
	 */
	@PostMapping(path = "/update", consumes = "application/json", produces = "application/json")
	public String processUpdate(@RequestBody Map<String, Object> productUpdate) {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(null, "markedproduct", "/update", "POST"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * 
	 * @param markedProduct consists of: { "buyerId", "sellerId", "price", "email",
	 *                      "itemTitle" }
	 * @return
	 */
	@PostMapping(path = "/mark", consumes = "application/json", produces = "application/json")
	public String markProduct(@RequestBody Map<String, Object> markedProduct) {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(null, "markedproduct", "/mark", "POST"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
