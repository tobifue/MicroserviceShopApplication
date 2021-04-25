package ase.gateway.controller;

import ase.gateway.traffic.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("checkout")
public class CheckoutController {
	private static final String serviceName = "checkout-service";

	@GetMapping("/checkout/{customerId}")
	@ResponseBody
	public String checkout(@PathVariable Long customerId) {
		System.out.println("/checkout in CheckoutController is called with ID: " + customerId);
		try {

			//der Teil sollte ins Service!!!!!


			String cart = TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "cart", String.format("/getCart/%s", customerId), "GET"));

			TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "cart", String.format("/deleteCart/%s", customerId), "GET"));

			System.out.println("/checkout in CheckoutController is called with ID: " + cart);


			JSONObject jsonObject = new JSONObject(cart);
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				Map<String, Object> item = new ObjectMapper().readValue(jsonArray.getJSONObject(i).toString(), HashMap.class);
				item.put("vendorId", customerId.toString());
				System.out.println(item); // display usernames
				TrafficController.sendMessageToSingleRecipient(
						Message.createInstance(item, "history", String.format("/add", customerId), "POST"));
				TrafficController.sendMessageToSingleRecipient(
						Message.createInstance(item, "shipment", String.format("/add", customerId), "POST"));
			}

			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "checkout", String.format("/checkout/%s", customerId), "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (JSONException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
