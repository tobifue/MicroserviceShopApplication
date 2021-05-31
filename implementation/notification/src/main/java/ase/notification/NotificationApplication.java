package ase.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ase.notification.data.Notification;
import ase.notification.data.NotificationRepository;
import ase.notification.data.NotificationService;

@EnableJpaRepositories("ase.notification.*")
@EntityScan("ase.notification.*")
@ComponentScan(basePackages = { "ase.notification.*" })
@RestController
@SpringBootApplication
public class NotificationApplication {

	// @Autowired
	private NotificationRepository repository;
	@Value("${server.port}")
	private String port;

	@Autowired
	private NotificationService notificationService;

	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}

	@RequestMapping(value = "/checkItems", method = RequestMethod.GET)
	public void printRepositoryToConsole() {
		System.out.println("All transactions in the repository");
		List<Notification> txs = repository.findAll();
		for (Notification t : txs)
			System.out.println(t);
	}

	@PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Notification addTransaction(@RequestBody Notification notification) {
		return repository.save(notification);
	}

	@RequestMapping(value = "/shipping/{itemId}/{shippingStatus}", method = RequestMethod.GET)
	public void callShipping(@PathVariable long itemId, @PathVariable String shippingStatus) {
		Notification nt = NotificationService.checkShipping(itemId, repository, shippingStatus);
		try {
			notificationService.sendEmail(nt);
		} catch (MailException e) {
			// catch
		}

	}

	@RequestMapping(value = "/price/{itemId}/{newPrice}", method = RequestMethod.GET)
	public void callPrice(@PathVariable long itemId, @PathVariable double newPrice) {
		Notification nt = NotificationService.checkPrice(itemId, repository, newPrice);
		try {
			notificationService.sendEmail(nt);
		} catch (MailException e) {
			// catch
		}

	}

	@RequestMapping(value = "/clearAll", method = RequestMethod.GET)
	public String clearAllTransactions() {
		repository.deleteAll();
		return "Cleared all transactions";
	}

	@RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
	@ResponseBody
	public String heartbeat() {
		return "OK";
	}

	private void registerWithGateway() {
		try {
			Map<String, Object> registrationDetails = new HashMap<>();
			registrationDetails.put("endpoints", new ArrayList<String>() {
				private static final long serialVersionUID = 1L;
				{
					// put highest level endpoints here
					add("/checkItems");
					add("/add");
					add("/shipping");
					add("/price");
					add("/clearAll");

				}
			});
			registrationDetails.put("category", "notification");
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
	public CommandLineRunner loadRepository(NotificationRepository repository) {
		return (args) -> {
			this.repository = repository;
			printRepositoryToConsole();
			// register with gateway in commandlineRunner
			registerWithGateway();
		};
	}

}
