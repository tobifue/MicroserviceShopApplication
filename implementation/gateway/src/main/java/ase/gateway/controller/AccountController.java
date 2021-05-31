package ase.gateway.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ase.gateway.controller.log.Log;
import ase.gateway.controller.log.LogRepository;
import ase.gateway.traffic.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("account")
public class AccountController {

	@Autowired
	private LogRepository logRepository;

	private static final String serviceName = "account-service";

	@GetMapping("/vendor/{id}")
	public String getProfitByVendorId(@PathVariable("id") Long vendorId) {
		try {
			return TrafficController.sendMessageToSingleRecipient(
					Message.createInstance(null, "account", String.format("/vendor/%s", vendorId), "GET"));
		} catch (RestClientException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
