package ase.gateway.controller;

import java.io.IOException;
import java.util.Map;

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
@RequestMapping("notification")
public class NotificationController {

	private static final String serviceName = "notification";

	@RequestMapping(value = "/checkItems", method = RequestMethod.GET)
	@ResponseBody
	public String checkItems() {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), "checkItems");
		} catch (RestClientException e) {
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/checkPrice/{itemId}", method = RequestMethod.GET)
	@ResponseBody
	public String checkPrice(@PathVariable long itemId) {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), String.format("checkPrice/%s", itemId));
		} catch (RestClientException e) {
			return e.getMessage();
		} catch (IOException e) {
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
			return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "add", notification);
		} catch (RestClientException | IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
