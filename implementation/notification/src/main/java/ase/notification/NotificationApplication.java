package ase.notification;

import java.net.InetAddress;
import java.net.UnknownHostException;
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

	@RequestMapping(value = "/shipping/{itemName}/{shippingStatus}/{email}", method = RequestMethod.GET)
	public void callShipping(@PathVariable String itemName, @PathVariable String shippingStatus,
			@PathVariable String email) {
		Notification nt = NotificationService.checkShipping(itemName, repository, shippingStatus, email);
		try {
			notificationService.sendEmail(nt);
			System.out.println("Email sent successfully");
		} catch (MailException e) {
			// catch
		}

	}

	@RequestMapping(value = "/price/{itemName}/{price}/{newPrice}/{email}", method = RequestMethod.GET)
	public void callPrice(@PathVariable String itemName, @PathVariable double price, @PathVariable double newPrice,
			@PathVariable String email) {
		Notification nt = NotificationService.checkPrice(itemName, repository, price, newPrice, email);
		try {
			notificationService.sendEmail(nt);
			System.out.println("Email sent successfully");
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

	@RequestMapping(value = "/registerWithGateway", method = RequestMethod.GET)
	private boolean registerWithGateway() {
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

			String notificationAddress = "http://" + InetAddress.getLocalHost().getHostAddress() + ":8088";
			String gatewayIp = "http://"
					+ (System.getenv("GATEWAYIP") == null ? "localhost" : System.getenv("GATEWAYIP")) + ":8080";

			registrationDetails.put("category", "rating");
			registrationDetails.put("ip", notificationAddress);
			new RestTemplate().postForObject(String.format("%s/%s", gatewayIp, "/register/new"), registrationDetails,
					String.class);
			return true;
		} catch (RestClientException | UnknownHostException e) {
			System.err.println("Failed to connect to Gateway, please register manually or restart application");
			return false;
		}
	}

	@Bean
	public CommandLineRunner loadRepository(NotificationRepository repository) {
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
