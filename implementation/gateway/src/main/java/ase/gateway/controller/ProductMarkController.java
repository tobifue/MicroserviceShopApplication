package ase.gateway.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("productMark")
public class ProductMarkController {

	private static final String serviceName = "productMark";

	@PostMapping(path = "/markItem", consumes = "application/json", produces = "application/json")
	/**
	 * @param item in format { "costumer": "String", "item" : {"vendor" : "2",
	 *             "price" : "10", "quantity" : "1", "priceRecommendation" : "1"}} }
	 * @return (" { \ " command \ " : \ " Okay \ " } ") or ("{
	 *         \"command\":\"Error\"}")
	 */
	public String markItem(@RequestBody Object item) {
		System.out.println("/markItem in ProductMarkController is called" + item.toString());

		String outputJson = "{ \"command\":\"ToDo\"}";
		return outputJson;
	}

}
