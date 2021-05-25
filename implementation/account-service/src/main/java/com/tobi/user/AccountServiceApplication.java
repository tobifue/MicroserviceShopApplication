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

@RestController
@SpringBootApplication
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

	@Autowired
	private AccountService accountService;

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

	@RequestMapping(value = "/registerWithGateway", method = RequestMethod.GET)
	private void registerWithGateway() {
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
		} catch (RestClientException e) {
			System.err.println("Failed to connect to Gateway, please register manually or restart application");
		}
	}

	@Bean
	public CommandLineRunner registerWithGateWay() {
		return (args) -> {
			// register with gateway in commandlineRunner
			registerWithGateway();
		};
	}
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}

