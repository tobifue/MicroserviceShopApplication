package ase.markedproduct.controller;

import java.util.List;
import java.util.Map;

import ase.markedproduct.data.MarkedProduct;
import ase.markedproduct.data.MarkedProductRepository;

public class UpdateController {

	public void updateProduct(Map<String, Object> updateMessage, MarkedProductRepository repository) {
		List<MarkedProduct> markedProducts = repository.findAll();
		for (MarkedProduct m : markedProducts) {
			if (m.getSellerId().equals(updateMessage.get("sellerid"))
					&& m.getItemTitle().equals(updateMessage.get("itemTitle"))) {
				if (m.getPrice() < (double) updateMessage.get("price")) {
					System.out.println("Price alert triggered for product " + m.getItemTitle());
					System.out.println("old price: " + m.getPrice() + "; new price: " + updateMessage.get("price"));
					m.setPrice((double) updateMessage.get("price"));
					// TODO email notification
				}
			}
		}
	}

}
