package ase.notification.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@Autowired
	private JavaMailSender javaMailSender;

	public static Notification checkShipping(String itemName, NotificationRepository repository, String shippingStatus,
			String email) {
		Notification endNotification = null;
		String emailBody, emailSubject;
		Notification nt = new Notification();
		nt.setShippingStatus(shippingStatus);
		nt.setItemName(itemName);
		emailBody = String.format("The shipment status of your purchased item %s has been updated to: %s", itemName,
				shippingStatus);
		emailSubject = "Shipment Notification";
		endNotification = nt;
		nt.setEmail(email);
		nt.setEmailBody(emailBody);
		nt.setEmailSubject(emailSubject);
		return endNotification;
	}

	public static Notification checkPrice(String itemName, NotificationRepository repository, double price,
			double newPrice, String email) {
		Notification endNotification = null;
		String emailBody, emailSubject;
		Notification nt = new Notification();
		nt.setItemName(itemName);
		emailBody = String.format("The price of your marked item %s has changed from %s EUR to %s EUR", itemName,
				Double.toString(price), Double.toString(newPrice));
		emailSubject = "Price Change Notification";
		endNotification = nt;
		nt.setEmail(email);
		nt.setEmailBody(emailBody);
		nt.setEmailSubject(emailSubject);
		nt.setNewPrice(newPrice);
		return endNotification;
	}

	public void sendEmail(Notification notification) throws MailException {
		SimpleMailMessage msg = new SimpleMailMessage();
		try {
			String email = notification.getEmail();
			msg.setTo(email);
			msg.setFrom("ase.notification@gmail.com");
			msg.setSubject(notification.getEmailSubject());
			msg.setText(notification.getEmailBody());
			javaMailSender.send(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
