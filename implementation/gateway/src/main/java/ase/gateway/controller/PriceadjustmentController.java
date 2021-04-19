package ase.gateway.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import ase.gateway.util.AdressUtil;
import ase.gateway.util.NetworkUtil;

@RestController
@RequestMapping("priceadjust")
public class PriceadjustmentController {

	private static String serviceName = "priceadjust";

	/**
	 * 
	 * @param requires { "item" : [insert item title] }
	 * @return
	 */
	@PostMapping(path = "/recommend", consumes = "application/json", produces = "application/json")
	public String getPriceRecommendation(@RequestBody Map<String, Object> markedProduct) {
		try {
			return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "/update", markedProduct);
		} catch (RestClientException | IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
