package com.tobi.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.tobi.user.service.AccountService;

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
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(accountService.getItemsByVendorId(vendorId));
		return json;
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}

