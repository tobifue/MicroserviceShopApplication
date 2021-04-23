package ase.priceadjustment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ase.priceadjustment.scraper.PriceScraper;

@RestController
@SpringBootApplication
public class PriceadjustmentApplication {

	private PriceScraper scraper;

	public static void main(String[] args) {
		SpringApplication.run(PriceadjustmentApplication.class, args);
	}

	@Value("${server.port}")
	private String port;

	@PostMapping(path = "/recommend", consumes = "application/json", produces = "application/json")
	public Double addTransaction(@RequestBody String item) {
		return scraper.scrape(item);
	}

	private void registerWithGateway() {
		try {
			Map<String, Object> registrationDetails = new HashMap<>();
			registrationDetails.put("endpoints", new ArrayList<String>() {
				private static final long serialVersionUID = 1L;
				{
					// put highest level endpoints here
					add("/recommend");

				}
			});
			registrationDetails.put("category", "pricecrawler");
			registrationDetails.put("ip", "http://localhost:" + port);
			new RestTemplate().postForObject(String.format("%s/%s", "http://localhost:8080", "/register/new"),
					registrationDetails, String.class);
		} catch (RestClientException e) {
			System.out.println("Could not reach Gateway, retrying in 5 seconds");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			registerWithGateway();
		}
		System.out.println("Successfully registered with gateway!");
	}

	@Bean
	public CommandLineRunner initialize() {
		return (args) -> {
			this.scraper = new PriceScraper();
		};
	}

}
