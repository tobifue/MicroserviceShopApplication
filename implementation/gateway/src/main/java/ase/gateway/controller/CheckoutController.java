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

	@GetMapping("/checkout/{costumerId}")
	@ResponseBody
	public String checkout(@PathVariable Long costumerId) {
		System.out.println("/checkout in CheckoutController is called with ID: " + costumerId);
		try {
			String cart = TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "cart", String.format("/getCart/%s", costumerId), "GET"));

			TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "cart", String.format("/deleteCart/%s", costumerId), "GET"));

			System.out.println("/checkout in CheckoutController is called with ID: " + cart);

			/*TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "history", String.format("/add"), "POST"));
*/

			JSONObject jsonObject = new JSONObject(cart);
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				Map<String, Object> item = new ObjectMapper().readValue(jsonArray.getJSONObject(i).toString(), HashMap.class);
				item.put("vendorId", costumerId.toString());
				System.out.println(item); // display usernames
				TrafficController.sendMessageToSingleRecipient(
						Message.createInstance(item, "history", String.format("/add", costumerId), "POST"));
				TrafficController.sendMessageToSingleRecipient(
						Message.createInstance(item, "shipment", String.format("/add", costumerId), "POST"));
			}
			//Map<String, Object> result = new ObjectMapper().readValue(cart, HashMap.class);
			//Map<String, Object> sol = new ObjectMapper().readValue(jsonArray.getJSONObject(i), HashMap.class);
			//System.out.println("nested array" + costumerId);

			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "checkout", String.format("/checkout/%s", costumerId), "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} /*catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}*/ catch (JSONException e) {
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
