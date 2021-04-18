package ase.gateway.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rateItem")
public class RateItemController {

	private static final String serviceName = "rateItem";

	@PostMapping(path = "/rateItem", consumes = "application/json", produces = "application/json")
	/**
	 * @param item in format { "costumer": "String", "item" : {"vendor" : "2",
	 *             "price" : "10", "quantity" : "1", "priceRecommendation" : "1"}} }
	 * @return (" { \ " command \ " : \ " Okay \ " } ") or ("{
	 *         \"command\":\"Error\"}")
	 */
	public String rateItem(@RequestBody Object item) {
		System.out.println("/rateItem in RateItemController is called" + item.toString());

		String outputJson = "{ \"command\":\"ToDo\"}";
		return outputJson;
	}
}
