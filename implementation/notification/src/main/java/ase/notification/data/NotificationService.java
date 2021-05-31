package ase.notification.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@Autowired
	private JavaMailSender javaMailSender;

	public static Notification checkShipping(long itemId, NotificationRepository repository, String shippingStatus) {
		Notification endNotification = null;
		String itemName, emailBody, emailSubject;
		long id;
		List<Notification> allNt = repository.findAll();
		for (Notification nt : allNt) {
			id = nt.getItemId();
			if (itemId == id) {
				nt.setShippingStatus(shippingStatus);
				itemName = nt.getItemName();
				emailBody = String.format("The shipment status of your purchased item %s has been updated to: %s",
						itemName, shippingStatus);
				emailSubject = "Shipment Notification";
				endNotification = nt;
				nt.setEmailBody(emailBody);
				nt.setEmailSubject(emailSubject);
			}

		}
		return endNotification;
	}

	public static Notification checkPrice(long itemId, NotificationRepository repository, double newPrice) {
		Notification endNotification = null;
		String itemName, emailBody, emailSubject;
		long id;
		double price;
		List<Notification> allNt = repository.findAll();
		for (Notification nt : allNt) {
			id = nt.getItemId();
			itemName = nt.getItemName();
			price = nt.getPrice();
			if (newPrice < price && itemId == id) {
				emailBody = String.format("The price of your marked item %s has changed from %s EUR to %s EUR",
						itemName, Double.toString(price), Double.toString(newPrice));
				emailSubject = "Price Change Notification";
				endNotification = nt;
				nt.setEmailBody(emailBody);
				nt.setEmailSubject(emailSubject);
				nt.setNewPrice(newPrice);
			}
		}
		return endNotification;
	}

	public void sendEmail(Notification notification) throws MailException {
		SimpleMailMessage msg = new SimpleMailMessage();
		try {
			msg.setTo(notification.getEmail());
			msg.setFrom("ase.notification@gmail.com");
			msg.setSubject(notification.getEmailSubject());
			msg.setText(notification.getEmailBody());
			javaMailSender.send(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
