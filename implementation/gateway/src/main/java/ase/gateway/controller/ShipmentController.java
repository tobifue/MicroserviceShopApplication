package ase.gateway.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import ase.gateway.traffic.Message;

@RestController
@RequestMapping("shipment")
public class ShipmentController {

	private static final String category = "shipment";

	@RequestMapping(value = "/show/item/{itemid}", method = RequestMethod.GET)
	@ResponseBody
	public String showShippingStatusForItem(@PathVariable Long itemid) {
		try {
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, category, String.format("/show/item/%s", itemid), "GET"));
		} catch (RestClientException e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/show/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String showAllShipmentsForUserId(@PathVariable Long userid) {
		try {
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, category, String.format("/show/%s", userid), "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	@ResponseBody
	public String showAllShipments() {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(null, category, "/show", "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * @param shipment in format { "buyerId", "sellerId", "itemid", "email",
	 *                 "price", "itemTitle", "count" }
	 * @return the shipment
	 */
	@PostMapping("/add")
	@ResponseBody
	public String addTransaction(@RequestBody Map<String, Object> shipment) {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(shipment, "shipment", String.format("/add"), "POST"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	@RequestMapping(value = "/start", method = RequestMethod.GET)
	@ResponseBody
	public String startShipmentService() {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(null, category, "/start", "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/stop", method = RequestMethod.GET)
	@ResponseBody
	public String stopShipmentService() {
		try {
			return TrafficController
					.sendMessageToSingleRecipient(Message.createInstance(null, category, "/stop", "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}