package ase.gateway.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import ase.gateway.util.AdressUtil;
import ase.gateway.util.NetworkUtil;

@RestController
@RequestMapping("shipment")
public class ShipmentController {

	private static final String serviceName = "shipment";

	@RequestMapping(value = "/show/item/{itemid}", method = RequestMethod.GET)
	@ResponseBody
	public String showShippingStatusForItem(@PathVariable Long itemid) {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), String.format("show/item/%s", itemid));
		} catch (RestClientException e) {
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/show/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String showAllShipmentsForUserId(@PathVariable Long userid) {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), String.format("show/%s", userid));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	@ResponseBody
	public String showAllShipments() {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), "show");
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * @param shipment in format { "buyerId", "sellerId", "itemid", "email",
	 *                 "price", "itemTitle", "count" }
	 * @return the shipment
	 */
	@PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
	public String addTransaction(@RequestBody Map<String, Object> shipment) {
		try {
			return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "add", shipment);
		} catch (RestClientException | IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/start", method = RequestMethod.GET)
	@ResponseBody
	public String startShipmentService() {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), "start");
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/stop", method = RequestMethod.GET)
	@ResponseBody
	public String stopShipmentService() {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), "stop");
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}