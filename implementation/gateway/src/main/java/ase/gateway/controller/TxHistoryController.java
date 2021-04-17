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

	private static final String prefix = "";
	private static final String serviceName = "txhistory";

	@RequestMapping(value = prefix + "/generate/{type}/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String generateHistory(@PathVariable String type, @PathVariable Long userid) {
		System.out.println("generate/{type}/{userid}");
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

	@RequestMapping(value = prefix + "/generate/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String generateHistory(@PathVariable Long userid) {
		System.out.println("generate/userid}");
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

	@RequestMapping(value = prefix + "/generate", method = RequestMethod.GET)
	@ResponseBody
	public String generateHistory() {
		System.out.println("generate");
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

	@PostMapping(path = prefix + "/add", consumes = "application/json", produces = "application/json")
	public String addTransaction(@RequestBody Map<String, Object> transaction) {
		System.out.println("add");
		try {
			return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "add", transaction);
		} catch (RestClientException | IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	// TODO clearAll functionality
}
