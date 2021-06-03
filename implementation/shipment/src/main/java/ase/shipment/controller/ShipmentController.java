package ase.shipment.controller;

import java.util.List;
import java.util.Random;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ase.shipment.data.Shipment;
import ase.shipment.data.ShipmentRepository;

public class ShipmentController {

	private Thread shipmentThread;
	private ShipmentRepository repository;

	public ShipmentController(ShipmentRepository repository) {
		this.repository = repository;
		shipmentThread = new Thread(() -> {
			while (true) {
				try {
					updateRandomShipment();
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					stop();
				}
			}
		});
	}

	public static String httpGet(String serviceAddress, String endpoint) throws RestClientException {
		return new RestTemplate().getForObject(String.format("%s/%s", serviceAddress, endpoint), String.class);
	}

	private void updateRandomShipment() {
		List<Shipment> shipments = repository.findAll();
		Shipment shipment = shipments.get(new Random().nextInt(shipments.size()));
		System.out.println(shipment);
		shipment.updateShippingStatus();
		System.out.println("new status " + shipment.getShippingStatus());
		// send notification
		String gatewayIp = "http://localhost:8080";
		String shippingStatus = shipment.getShippingStatus();
		String itemName = shipment.getItemName();
		String email = shipment.getEmail();
		httpGet(gatewayIp, String.format("/notification/shipping/%s/%s/%s", itemName, shippingStatus, email));

	}

	public void updateShipment(Shipment shipment) {
		System.out.println(shipment);
		shipment.updateShippingStatus();
		System.out.println("new status " + shipment.getShippingStatus());
	}

	public void start() {
		shipmentThread.start();
	}

	public void stop() {
		shipmentThread.interrupt();
	}

}
