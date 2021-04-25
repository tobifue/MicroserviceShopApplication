package ase.markedproduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ase.markedproduct.controller.UpdateController;
import ase.markedproduct.data.MarkedProduct;
import ase.markedproduct.data.MarkedProductRepository;

@EnableJpaRepositories("ase.markedproduct.*")
@EntityScan("ase.markedproduct.*")
@ComponentScan(basePackages = { "ase.markedproduct.*" })
@RestController
@SpringBootApplication
public class MarkedproductApplication {

	private MarkedProductRepository repository;
	private UpdateController updateController;

	public static void main(String[] args) {
		SpringApplication.run(MarkedproductApplication.class, args);
	}

	@Value("${server.port}")
	private String port;

	@RequestMapping(value = "/show/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public List<MarkedProduct> showAllProductsForUser(@PathVariable Long userid) {
		List<MarkedProduct> result = new ArrayList<>();
		for (MarkedProduct item : repository.findAll()) {
			if (item.getVendorId().equals(userid) || item.getCustomerId().equals(userid))
				result.add(item);
		}
		return result;
	}

	@RequestMapping(value = "/showall", method = RequestMethod.GET)
	@ResponseBody
	public List<MarkedProduct> showAllMarkedProducts() {
		return repository.findAll();
	}

	/**
	 * @param message contains information about the update with fields "sellerid",
	 *                "itemTitle", "price"
	 * 
	 */
	@PostMapping(path = "/update", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void processUpdate(@RequestBody Map<String, Object> message) {
		updateController.updateProduct(message, repository);
	}

	@PostMapping(path = "/mark", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public MarkedProduct markItem(@RequestBody MarkedProduct markedProduct) {
		return repository.save(markedProduct);
	}

	@RequestMapping(value = "/print", method = RequestMethod.GET)
	public void printRepositoryToConsole() {
		System.out.println("All transactions in the repository");
		List<MarkedProduct> items = repository.findAll();
		for (MarkedProduct t : items)
			System.out.println(t);
	}

	private void registerWithGateway() {
		try {
			Map<String, Object> registrationDetails = new HashMap<>();
			registrationDetails.put("endpoints", new ArrayList<String>() {
				private static final long serialVersionUID = 1L;
				{
					// put highest level endpoints here
					add("/show");
					add("/showall");
					add("/update");
					add("/mark");
					add("/print");
				}
			});
			registrationDetails.put("category", "markedproduct");
			registrationDetails.put("ip", "http://localhost:" + port);
			System.out.println(port);
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
	public CommandLineRunner loadRepository(MarkedProductRepository repository) {
		return (args) -> {
			registerWithGateway();
			this.repository = repository;
			this.updateController = new UpdateController();
			printRepositoryToConsole();
		};
	}

}
