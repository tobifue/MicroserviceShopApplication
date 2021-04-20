package ase.gateway.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import ase.gateway.util.AdressUtil;
import ase.gateway.util.NetworkUtil;

@RestController
@RequestMapping("account")
public class AccountController {

	private static final String serviceName = "account-service";

	@GetMapping("/vendor/{id}")
	public String getProfitByVendorId(@PathVariable("id") Long vendorId) {
		try {
			return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), String.format("/vendor/%s", vendorId));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
