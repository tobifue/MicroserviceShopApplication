package ase.notification.data;

import java.util.List;

public class NotificationService {

	public static void checkShipping(long itemId, NotificationRepository repository) {
		String itemName, email, shippingStatus, emailBody;
		List<Notification> allNt = repository.findAll();
		for (Notification nt : allNt) {
			email = nt.getEmail();
			itemName = nt.getItemName();
			emailBody = nt.getEmailBody();
			shippingStatus = nt.getShippingStatus();
			String finalEmail = String
					.format(emailBody + shippingStatus + " for Item: " + itemName + " sendto: " + email);
			System.out.println(finalEmail);

		}
	}

	public static void checkPrice(long itemId, NotificationRepository repository, double newPrice) {
		String itemName, email, emailBody;
		double price;
		List<Notification> allNt = repository.findAll();
		for (Notification nt : allNt) {
			email = nt.getEmail();
			itemName = nt.getItemName();
			emailBody = nt.getEmailBody();
			price = nt.getPrice();
			if (newPrice < price) {
				String finalEmail = String.format(emailBody + price + " for Item: " + itemName + " sendto: " + email);
				System.out.println(finalEmail);

			}

		}
	}

}
