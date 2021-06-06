package ase.priceadjustment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ase.priceadjustment.scraper.PriceScraper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PriceScraperTest {

	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;

	private PriceScraper scraper;

	@BeforeAll
	public static void setup() {

	}

	@BeforeEach
	public void initialize() {
		scraper = new PriceScraper(); // Arrange
		this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	@DisplayName("Crawl for price of an iPhone 12 mini")
	public void scrapeForPrice() throws Exception {

		Map<String, Object> recommend = null;
		recommend.put("itemName", "iPhone 12 mini");
		String response = scraper.scrape(recommend); // Act

		assertEquals(new String("EUR 751,06"), response); // Assert

	}

}
