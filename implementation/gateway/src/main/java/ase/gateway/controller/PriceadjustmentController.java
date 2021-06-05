package ase.gateway.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import ase.gateway.traffic.Message;

@RestController
@RequestMapping("pricecrawler")
public class PriceadjustmentController {

	private static String serviceName = "pricecrawler";

	/**
	 * 
	 * @param requires { "item" : [insert item title] }
	 * @return
	 */
	@PostMapping(path = "/recommend", consumes = "text/plain", produces = "text/plain")
	public String getPriceRecommendation(@RequestBody String itemName) {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(itemName, "pricecrawler", "/recommend", "POST"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
