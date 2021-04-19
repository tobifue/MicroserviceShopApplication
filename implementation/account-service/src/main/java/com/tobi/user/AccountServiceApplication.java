package com.tobi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import com.tobi.user.service.AccountService;

@SpringBootApplication
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

	@Autowired
	private AccountService accountService;

	@GetMapping("/{id}")
	public Double getProfitByVendorId(@PathVariable("id") Long vendorId){
		return accountService.getItemsByVendorId(vendorId);
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}

