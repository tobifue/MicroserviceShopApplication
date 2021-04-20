package ase.rating;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ase.rating.data.Rating;
import ase.rating.data.RatingRepository;
import ase.rating.data.RatingService;

@EnableJpaRepositories("ase.rating.*")
@EntityScan("ase.rating.*")
@ComponentScan(basePackages = { "ase.rating.*" })
@RestController
@SpringBootApplication
public class RatingApplication {

	@Autowired
	private RatingRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(RatingApplication.class, args);
	}

	@RequestMapping(value = "/rating/{itemId}", method = RequestMethod.GET)
	@ResponseBody
	public String getRating(@PathVariable String itemId) {
		return RatingService.getRating(repository, itemId);
	}

	@PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Rating addTransaction(@RequestBody Rating rating) {
		return repository.save(rating);
	}

	@RequestMapping(value = "/checkRatings", method = RequestMethod.GET)
	public void printRepositoryToConsole() {
		System.out.println("All transactions in the repository");
		List<Rating> txs = repository.findAll();
		for (Rating r : txs)
			System.out.println(r);
	}
}
