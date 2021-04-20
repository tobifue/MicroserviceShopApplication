package ase.notification;

import java.util.ArrayList;
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

import ase.notification.data.Notification;
import ase.notification.data.NotificationRepository;

@EnableJpaRepositories("ase.notification.*")
@EntityScan("ase.notification.*")
@ComponentScan(basePackages = { "ase.notification.*" })
@RestController
@SpringBootApplication
public class NotificationApplication {

	@Autowired
	private NotificationRepository repository;

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

	@RequestMapping(value = "/checkPrice/{itemId}", method = RequestMethod.GET)
	public void checkPrice(@PathVariable long itemId) {
		double newPrice, price;
		String itemTitle, buyerEmail;
		List<Notification> allNt = repository.findAll();
		List<Notification> result = new ArrayList<>();
		for (Notification nt : allNt) {
			if (nt.getItemId().equals(itemId)) {
				price = nt.getPrice();
				newPrice = 3;
				if (newPrice < price)
					result.add(nt);
				buyerEmail = nt.getEmail();
				itemTitle = nt.getItemTitle();
				sendNotification(newPrice, price, buyerEmail, itemTitle);
			}
		}
	}

	@PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Notification addTransaction(@RequestBody Notification notification) {
		return repository.save(notification);
	}

	public static void sendNotification(double newPrice, double price, String buyerEmail, String itemTitle) {
		String finalNotification = String.format("Item %s now costs %f instead of %f", itemTitle, newPrice, price);
		System.out.println(finalNotification);

	}

}
