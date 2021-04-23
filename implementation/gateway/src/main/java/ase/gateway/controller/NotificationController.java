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
@RequestMapping("notification")
public class NotificationController {

	// private static final String serviceName = "notification";

	@RequestMapping(value = "/checkItems", method = RequestMethod.GET)
	@ResponseBody
	public String checkItems() {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(null, "notification", "/checkItems", "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/checkPrice/{itemId}", method = RequestMethod.GET)
	@ResponseBody
	public String checkPrice(@PathVariable long itemId) {
		try {
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "notification", String.format("/checkPrice/%s", itemId), "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * @param transaction in format { "buyerId" : "1", "itemId": "2", "price": "10",
	 *                    "itemTitle": "item1" }
	 * @return the transaction
	 */
	@PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
	public String addTransaction(@RequestBody Map<String, Object> notification) {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(notification, "notification", "/add", "POST"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
