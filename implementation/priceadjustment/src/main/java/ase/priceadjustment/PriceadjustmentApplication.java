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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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

	@PostMapping(path = "/recommend", consumes = "text/plain", produces = "text/plain")
	public String addTransaction(@RequestBody String item) {
		return scraper.scrape(item);
	}

	@RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
	@ResponseBody
	public String heartbeat() {
		return "OK";
	}

	@RequestMapping(value = "/registerWithGateway", method = RequestMethod.GET)
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
			System.out.println("Successfully registered with gateway!");
		} catch (RestClientException e) {
			System.err.println("Failed to connect to Gateway, please register manually or restart application");
		}
	}

	@Bean
	public CommandLineRunner initialize() {
		return (args) -> {
			this.scraper = new PriceScraper();
			registerWithGateway();
		};
	}

}
