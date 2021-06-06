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

import java.net.InetAddress;
import java.net.UnknownHostException;
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

}

