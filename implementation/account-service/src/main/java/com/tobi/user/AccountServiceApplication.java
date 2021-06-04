package com.tobi.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.tobi.user.service.AccountService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Account service application class with main, endpoints and gateway registration.
 */
@RestController
@SpringBootApplication
public class AccountServiceApplication {

	/**
	 * Bootstraps spring application as stand-alone application
	 */
	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

	@Autowired
	private AccountService accountService;

	/**
	 * GET method endpoint to get profit by vendorID. Returns
	 * json with plain double value.
	 */
	@SneakyThrows
	@GetMapping(path = "/vendor/{id}", produces="application/json")
	public String getProfitByVendorId(@PathVariable("id") Long vendorId){
		String json = "";
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			json = ow.writeValueAsString(accountService.getItemsByVendorId(vendorId));
		} catch (Exception e) {
			System.out.println("Something went wrong.");
		}
		return json;
	}

	@Value("${server.port}")
	private String port;

	/**
	 * Endpoint to register service with gateway service.
	 * Registration details (port, category) are set.
	 */
	@RequestMapping(value = "/registerWithGateway", method = RequestMethod.GET)
	private boolean registerWithGateway() {
		try {
			Map<String, Object> registrationDetails = new HashMap<>();
			registrationDetails.put("endpoints", new ArrayList<String>() {
				private static final long serialVersionUID = 1L;
				{
					// put highest level endpoints here
					add("/vendor");
				}
			});
			registrationDetails.put("category", "account");
			registrationDetails.put("ip", "http://localhost:" + port);
			new RestTemplate().postForObject(String.format("%s/%s", "http://localhost:8080", "/register/new"),
					registrationDetails, String.class);
			System.out.println("Successfully registered with gateway!");
			return true;
		} catch (RestClientException e) {
			System.err.println("Failed to connect to Gateway, please register manually or restart application");
			return false;
		}
	}

	/**
	 * Endpoint to register service with gateway service.
	 * Registration details (port, category) are set.
	 */
	@RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
	@ResponseBody
	public String heartbeat() {
		return "OK";
	}

	@Bean
	public CommandLineRunner registerWithGateWay() {
		return (args) -> {
			// register with gateway in commandlineRunner
			registerWithGateway();
		};
	}

	@Bean
	public CommandLineRunner registerAgainWithGateway( ) {
		return (args) -> {
			new Thread(() -> {
				while (!registerWithGateway()) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		};
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}

