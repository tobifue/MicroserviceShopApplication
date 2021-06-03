package ase.markedproduct.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ase.markedproduct.data.MarkedProduct;
import ase.markedproduct.data.MarkedProductRepository;

public class UpdateController {

	public static String httpGet(String serviceAddress, String endpoint) throws RestClientException {
		return new RestTemplate().getForObject(String.format("%s/%s", serviceAddress, endpoint), String.class);
	}

	public void updateProduct(Map<String, Object> updateMessage, MarkedProductRepository repository) {
		List<MarkedProduct> markedProducts = repository.findAll();
		for (MarkedProduct m : markedProducts) {
			if (m.getVendorId().equals(updateMessage.get("vendorId"))
					&& m.getItemName().equals(updateMessage.get("itemName"))) {

				// check if price has changed
				if (m.getPrice() < (double) updateMessage.get("price")) {
					System.out.println("Price alert triggered for product " + m.getItemName());
					System.out.println("old price: " + m.getPrice() + "; new price: " + updateMessage.get("price"));

					// send notification
					String gatewayIp = "http://localhost:8080";
					String itemName = m.getItemName();
					double oldPrice = m.getPrice();
					double newPrice = (double) updateMessage.get("price");
					String email = m.getEmail();
					httpGet(gatewayIp,
							String.format("/notification/price/%s/%s/$s", itemName, oldPrice, newPrice, email));
				}

				// set new price
				repository.delete(m);

				m.setPrice((double) updateMessage.get("price"));
				repository.save(m);
			}
		}
	}

}
