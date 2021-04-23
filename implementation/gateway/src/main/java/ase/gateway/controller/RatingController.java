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
@RequestMapping("rating")
public class RatingController {

	// private static final String serviceName = "rating";

	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	@ResponseBody
	public String getRating(@PathVariable Long itemId) {
		try {
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "rating", String.format("/%s", itemId), "GET"));
		} catch (RestClientException e) {
			return e.getMessage();
		}
	}

	/**
	 * @param rating in format { "buyerId": 3, "itemId" : "1", "itemTitle": "Item2",
	 *               "rating": "2"}
	 * @return the transaction
	 */

	@PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
	public String addTransaction(@RequestBody Map<String, Object> rating) {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(rating, "rating", "/add", "POST"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}

	@RequestMapping(value = "/checkRatings", method = RequestMethod.GET)
	@ResponseBody
	public String checkRatings() {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(null, "rating", "/checkRatings", "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
