package ase.priceadjustment;

import java.net.InetAddress;
import java.net.UnknownHostException;
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

	@PostMapping(path = "/recommend", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String addTransaction(@RequestBody Map<String, Object> recommend) {
		return scraper.scrape(recommend);
	}

	@RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
	@ResponseBody
	public String heartbeat() {
		return "OK";
	}

	@RequestMapping(value = "/registerWithGateway", method = RequestMethod.GET)
	private boolean registerWithGateway() {
		try {
			Map<String, Object> registrationDetails = new HashMap<>();
			registrationDetails.put("endpoints", new ArrayList<String>() {
				private static final long serialVersionUID = 1L;
				{
					// put highest level endpoints here
					add("/recommend");

				}
			});
			String thisAdr = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port;
			registrationDetails.put("category", "pricecrawler");
			registrationDetails.put("ip", thisAdr);
			String gatewayIp = "http://"
					+ (System.getenv("GATEWAYIP") == null ? "localhost" : System.getenv("GATEWAYIP")) + ":8080";

			new RestTemplate().postForObject(String.format("%s/%s", gatewayIp, "/register/new"), registrationDetails,
					String.class);
			return true;
		} catch (RestClientException | UnknownHostException e) {
			System.err.println("Failed to connect to Gateway, please register manually or restart application");
			return false;
		}
	}

	@Bean
	public CommandLineRunner loadRepository() {
		return (args) -> {
			this.scraper = new PriceScraper();
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

}
