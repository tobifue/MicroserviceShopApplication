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
@RequestMapping("history")
public class TxHistoryController {

	/**
	 * @param type   : either "buyer" or "seller", lists all transactions where user
	 *               appears as buyer or seller respectively
	 * @param userid : the userid of the user
	 * @return lists all transactions where the user was involved in in a specific
	 *         role
	 */
	@RequestMapping(value = "/generate/{type}/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String generateHistory(@PathVariable String type, @PathVariable Long userid) {
		try {
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "history", String.format("/generate/%s/%s", type, userid), "GET"));
		} catch (RestClientException e) {
			return e.getMessage();
		}
	}

	/**
	 * @param
	 * @return lists all transactions where the user was involved in
	 */
	@RequestMapping(value = "/generate/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String generateHistory(@PathVariable Long userid) {
		try {
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "history", String.format("/generate/%s", userid), "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * @return a list of all transactions
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	@ResponseBody
	public String generateHistory() {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(null, "history", "/generate", "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * @param transaction in format { "buyerId" : "1", "sellerId": "2", "price":
	 *                    "10", "itemTitle": "nice product", "count" : "1" }
	 * @return the transaction
	 */
	@PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
	public String addTransaction(@RequestBody Map<String, Object> transaction) {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(transaction, "history", "/add", "POST"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
