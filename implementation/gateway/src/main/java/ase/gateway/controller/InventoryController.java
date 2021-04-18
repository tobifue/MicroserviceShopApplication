package ase.gateway.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("inventory")
public class InventoryController {

	private static final String serviceName = "inventory";

	@RequestMapping(value = "/getItems/{vendorid}", method = RequestMethod.GET)
	/**
	 * @return format of {"items":[{"name": string, "quantity": number, "price":
	 *         number, "vendor": string, "priceRecommendation": number}]}
	 */
	public String getItems(@PathVariable Long vendorid) {
		System.out.println("/getItems(id) in InventoryController is called");
		String outputJson = "{ \"command\":\"ToDo\"}";
		return outputJson;
	}

	@RequestMapping(value = "/getItems", method = RequestMethod.GET)
	/**
	 * @return format of {"items":[{"name": string, "quantity": number, "price":
	 *         number, "vendor": string, "priceRecommendation": number}]}
	 */
	public String getItems() {
		System.out.println("/getItems in InventoryController is called");
		String outputJson = "{ \"command\":\"ToDo\"}";
		return outputJson;
	}

	@PostMapping(path = "/addItem", consumes = "application/json", produces = "application/json")
	/**
	 * @param item in format { "item" : {"vendor" : "2", "price" : "10", "quantity"
	 *             : "1", "priceRecommendation" : "1"}} }
	 * @return (" { \ " command \ " : \ " Okay \ " } ") or ("{
	 *         \"command\":\"Error\"}")
	 */
	public String addItem(@RequestBody Object item) {
		System.out.println("/addItem in InventoryController is called with" + item.toString());
		String outputJson = "{ \"command\":\"ToDo\"}";
		return outputJson;
	}
}
