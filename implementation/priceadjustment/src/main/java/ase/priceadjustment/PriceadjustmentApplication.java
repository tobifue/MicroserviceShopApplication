package ase.priceadjustment;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ase.priceadjustment.scraper.PriceScraper;

@RestController
@SpringBootApplication
public class PriceadjustmentApplication {

	private PriceScraper scraper;

	public static void main(String[] args) {
		SpringApplication.run(PriceadjustmentApplication.class, args);
	}

	@PostMapping(path = "/recommend", consumes = "application/json", produces = "application/json")
	public Double addTransaction(@RequestBody String item) {
		return scraper.scrape(item);
	}

	@Bean
	public CommandLineRunner initialize() {
		return (args) -> {
			this.scraper = new PriceScraper();
		};
	}

}
