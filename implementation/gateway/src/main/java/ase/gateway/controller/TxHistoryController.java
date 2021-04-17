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
@RequestMapping("history")
public class TxHistoryController {

	private static final String serviceName = "txhistory";

	@RequestMapping(value = "/generate/{type}/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String generateHistory(@PathVariable String type, @PathVariable Long userid) {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName),
					String.format("generate/%s/%s", type, userid));
		} catch (RestClientException e) {
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/generate/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String generateHistory(@PathVariable Long userid) {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), String.format("generate/%s", userid));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	@ResponseBody
	public String generateHistory() {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), "generate");
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
	public String addTransaction(@RequestBody Map<String, Object> transaction) {
		try {
			return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "add", transaction);
		} catch (RestClientException | IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	// TODO clearAll functionality
}
