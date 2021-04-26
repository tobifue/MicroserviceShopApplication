package ase.shipment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ase.shipment.controller.ShipmentController;
import ase.shipment.data.Shipment;
import ase.shipment.data.ShipmentRepository;
import net.minidev.json.JSONArray;

@EnableJpaRepositories("ase.shipment.*")
@EntityScan("ase.shipment.*")
@ComponentScan(basePackages = { "ase.shipment.*" })
@RestController
@SpringBootApplication
public class ShipmentApplication {

	private ShipmentRepository repository;
	private ShipmentController controller;

	@Value("${server.port}")
	private String port;

	public static void main(String[] args) {
		SpringApplication.run(ShipmentApplication.class, args);
	}

	@RequestMapping(value = "/show/item/{itemid}", method = RequestMethod.GET)
	@ResponseBody
	public String showShippingStatusForItem(@PathVariable Long itemid) {
		for (Shipment s : repository.findAll()) {
			if (s.getItemId().equals(itemid))
				return s.toJsonObject().toJSONString();
		}
		return "no item with itemid " + itemid + "could be found.";
	}

	@RequestMapping(value = "/show/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String showAllShipmentsForUserId(@PathVariable Long userid) {
		List<Shipment> result = new ArrayList<>();
		for (Shipment spmt : repository.findAll()) {
			if (spmt.getCustomerId().equals(userid) || spmt.getVendorId().equals(userid))
				result.add(spmt);
		}
		JSONArray j = new JSONArray();
		for (Shipment t : result)
			j.add(t.toJsonObject());
		return j.toJSONString();
	}

	@RequestMapping(value = "/start", method = RequestMethod.GET)
	@ResponseBody
	public void startController() {
		controller.start();
	}

	@RequestMapping(value = "/stop", method = RequestMethod.GET)
	@ResponseBody
	public void stopController() {
		controller.stop();
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	@ResponseBody
	public List<Shipment> showAllShipments() {
		return repository.findAll();
	}

	@PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Shipment addTransaction(@RequestBody Shipment shipment) {
		return repository.save(shipment);
	}

	@RequestMapping(value = "/print", method = RequestMethod.GET)
	public void printRepositoryToConsole() {
		System.out.println("All shipments in the repository");
		List<Shipment> s = repository.findAll();
		for (Shipment t : s)
			System.out.println(t);
	}

	@RequestMapping(value = "/clearAll", method = RequestMethod.GET)
	public String clearAllTransactions() {
		repository.deleteAll();
		return "Cleared all transactions";
	}

	private void registerWithGateway() {
		try {
			Map<String, Object> registrationDetails = new HashMap<>();
			registrationDetails.put("endpoints", new ArrayList<String>() {
				private static final long serialVersionUID = 1L;
				{
					// put highest level endpoints here
					add("/show");
					add("/start");
					add("/stop");
					add("/clearAll");
					add("/print");
				}
			});
			registrationDetails.put("category", "shipment");
			registrationDetails.put("ip", "http://localhost:" + port);
			new RestTemplate().postForObject(String.format("%s/%s", "http://localhost:8080", "/register/new"),
					registrationDetails, String.class);
		} catch (RestClientException e) {
			System.out.println("Could not reach Gateway, retrying in 5 seconds");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			registerWithGateway();
		}
		System.out.println("Successfully registered with gateway!");
	}

	@Bean
	public CommandLineRunner loadRepository(ShipmentRepository repository) {
		return (args) -> {
			this.repository = repository;
			printRepositoryToConsole();
			this.controller = new ShipmentController(repository);
			registerWithGateway();
		};
	}

}
