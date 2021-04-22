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
@RequestMapping("rating")
public class RatingController {

	private static final String serviceName = "rating";

	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	@ResponseBody
	public String getRating(@PathVariable Long itemId) {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), String.format("/%s", itemId));
		} catch (RestClientException e) {
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
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
			return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "add", rating);
		} catch (RestClientException | IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}

	@RequestMapping(value = "/checkRatings", method = RequestMethod.GET)
	@ResponseBody
	public String checkRatings() {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), "checkRatings");
		} catch (RestClientException e) {
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
