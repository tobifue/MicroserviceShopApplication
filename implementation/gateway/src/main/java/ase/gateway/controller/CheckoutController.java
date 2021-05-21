package ase.gateway.controller;

import ase.gateway.traffic.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("checkout")
public class CheckoutController {
	private static final String serviceName = "checkout-service";

	@GetMapping(path = "/checkout/{customerId}", produces = "application/json")
	public String checkout(@PathVariable("customerId") Long customerId) {
		System.out.println("/checkout in CheckoutController is called with ID: " + customerId);
		try {
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "checkout", String.format("/checkout/%s", customerId), "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
