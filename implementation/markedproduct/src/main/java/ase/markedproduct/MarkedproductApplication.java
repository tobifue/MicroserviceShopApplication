package ase.markedproduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	@RequestMapping(value = "/show/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public List<MarkedProduct> showAllProductsForUser(@PathVariable Long userid) {
		List<MarkedProduct> result = new ArrayList<>();
		for (MarkedProduct item : repository.findAll()) {
			if (item.getBuyerId().equals(userid) || item.getSellerId().equals(userid))
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

	@Bean
	public CommandLineRunner loadRepository(MarkedProductRepository repository) {
		return (args) -> {
			this.repository = repository;
			this.updateController = new UpdateController();
			printRepositoryToConsole();
		};
	}

}