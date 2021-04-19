package ase.shipment.controller;

import java.util.List;
import java.util.Random;

import ase.shipment.data.Shipment;
import ase.shipment.data.ShipmentRepository;

public class ShipmentController {

	private Thread shipmentThread;

	public ShipmentController(ShipmentRepository repository) {
		shipmentThread = new Thread(() -> {
			while (true) {
				try {
					List<Shipment> shipments = repository.findAll();
					Shipment shipment = shipments.get(new Random().nextInt(shipments.size()));
					System.out.println(shipment);
					shipment.updateShippingStatus();
					System.out.println("new status " + shipment.getShippingStatus());
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					stop();
				}
			}
		});
	}

	public void start() {
		shipmentThread.start();
	}

	public void stop() {
		shipmentThread.interrupt();
	}

}
