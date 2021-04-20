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
@RequestMapping("markedproduct")
public class MarkedProductController {

	private static String serviceName = "markedproduct";

	@RequestMapping(value = "/show/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String showAllProductsForUser(@PathVariable Long userid) {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), String.format("/show/%s", userid));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/showall", method = RequestMethod.GET)
	@ResponseBody
	public String showAllMarkedProducts() {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), String.format("/showall"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
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
			return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "/update", productUpdate);
		} catch (RestClientException | IOException e) {
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
	@PostMapping(path = "/update", consumes = "application/json", produces = "application/json")
	public String markProduct(@RequestBody Map<String, Object> markedProduct) {
		try {
			return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "/update", markedProduct);
		} catch (RestClientException | IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
