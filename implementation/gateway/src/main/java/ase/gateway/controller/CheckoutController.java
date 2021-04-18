package ase.gateway.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("checkout")
public class CheckoutController {
	private static final String serviceName = "checkout";

	/**
	 * @param
	 * @return ists all transactions where the user was involved in
	 */
	@RequestMapping(value = "/checkout/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String checkout(@PathVariable Long userid) {
		System.out.println("/checkout in CheckoutController is called with ID: " + userid);
		String outputJson = "{ \"command\":\"ToDo\"}";
		return outputJson;
	}
}
