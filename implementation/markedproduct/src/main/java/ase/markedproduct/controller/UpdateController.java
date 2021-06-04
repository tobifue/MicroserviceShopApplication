package ase.markedproduct.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ase.markedproduct.data.MarkedProduct;
import ase.markedproduct.data.MarkedProductRepository;

public class UpdateController {

	public static String httpGet(String serviceAddress, String endpoint) throws RestClientException {
		return new RestTemplate().getForObject(String.format("%s/%s", serviceAddress, endpoint), String.class);
	}

	public String updateProduct(Map<String, Object> updateMessage, MarkedProductRepository repository) {
		List<MarkedProduct> markedProducts = repository.findAll();
		String result = "Product not found";
		for (MarkedProduct m : markedProducts) {

			if (m.getVendorId() == Long.parseLong((String) updateMessage.get("vendorId"))
					&& m.getItemName().equals(updateMessage.get("itemName"))) {

				// check if price has changed
				if (Double.parseDouble((String) updateMessage.get("price")) < m.getPrice()) {
					System.out.println("Price alert triggered for product " + m.getItemName());
					System.out.println("old price: " + m.getPrice() + "; new price: " + updateMessage.get("price"));
					result = String.format("price changed from %s to %s", m.getPrice(), updateMessage.get("price"));
					// send notification
					String gatewayIp = "http://localhost:8080";
					String itemName = m.getItemName();
					double oldPrice = m.getPrice();
					double newPrice = Double.parseDouble((String) updateMessage.get("price"));
					String email = m.getEmail();
					try {
						httpGet(gatewayIp,
								String.format("/notification/price/%s/%s/%s/%s", itemName, oldPrice, newPrice, email));
					} catch (HttpClientErrorException e) {
						System.err.println("Notificationservice offline; update successful");
					}
				}

				// set new price
				repository.delete(m);
				m.setPrice(Double.parseDouble((String) updateMessage.get("price")));
				repository.save(m);
			}
		}
		return result;
	}

}
