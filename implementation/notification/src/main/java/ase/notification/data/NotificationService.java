package ase.notification.data;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {

	public static List<Notification> checkPrice(NotificationRepository repository) {
		double newPrice, price;
		String itemTitle, buyerEmail;
		List<Notification> allNt = repository.findAll();
		List<Notification> result = new ArrayList<>();
		for (Notification nt : allNt) {
			price = nt.getPrice();
			newPrice = 3;
			if (newPrice < price)
				result.add(nt);
			buyerEmail = nt.getEmail();
			itemTitle = nt.getItemTitle();
			sendNotification(newPrice, price, buyerEmail, itemTitle);
		}
		return result;

	}

	public static void sendNotification(double newPrice, double price, String buyerEmail, String itemTitle) {
		String finalNotification = String.format("Item %s now costs %np instead of %p", itemTitle, newPrice, price);
		System.out.println(finalNotification);

	}

}
