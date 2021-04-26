package ase.gateway.controller;

import ase.gateway.traffic.Message;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.Map;

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
		System.out.println("markedProduct is called : " + markedProduct);
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(markedProduct, "markedproduct", "/mark", "POST"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
