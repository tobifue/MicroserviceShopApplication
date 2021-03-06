package ase.rating;

import java.net.InetAddress;
import java.net.UnknownHostException;
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

import ase.rating.data.Rating;
import ase.rating.data.RatingRepository;
import ase.rating.data.RatingService;

@EnableJpaRepositories("ase.rating.*")
@EntityScan("ase.rating.*")
@ComponentScan(basePackages = { "ase.rating.*" })
@RestController
@SpringBootApplication
public class RatingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatingApplication.class, args);
	}

	// @Autowired
	private RatingRepository repository;
	@Value("${server.port}")
	private String port;

	@RequestMapping(value = "/getRating/{itemName}", method = RequestMethod.GET)
	@ResponseBody
	public String getRating(@PathVariable String itemName) {
		return RatingService.getRating(repository, itemName);
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
					add("/checkRatings");
					add("/getRating");
					add("/add");
				}
			});

			String ratingAddress = "http://" + InetAddress.getLocalHost().getHostAddress() + ":8087";
			String gatewayIp = "http://"
					+ (System.getenv("GATEWAYIP") == null ? "localhost" : System.getenv("GATEWAYIP")) + ":8080";

			registrationDetails.put("category", "rating");
			registrationDetails.put("ip", ratingAddress);
			new RestTemplate().postForObject(String.format("%s/%s", gatewayIp, "/register/new"), registrationDetails,
					String.class);
			return true;
		} catch (RestClientException | UnknownHostException e) {
			System.err.println("Failed to connect to Gateway, please register manually or restart application");
			return false;
		}
	}

	@Bean
	public CommandLineRunner loadRepository(RatingRepository repository) {
		return (args) -> {
			this.repository = repository;
			printRepositoryToConsole();
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
