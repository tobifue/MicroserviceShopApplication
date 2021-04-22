package ase.gateway.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ase.gateway.traffic.ServiceConnection;

@RestController
@RequestMapping("register")
public class TrafficController {

	private List<ServiceConnection> activeConnections = new ArrayList<>();

	/**
	 * @param registration details in format { "categories" : ["category1",
	 *                     "category2"], "ip": "200.0.0.199:8030" }
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(path = "/new", consumes = "application/json", produces = "application/json")
	public void registerNewService(@RequestBody Map<String, Object> details) {
		ServiceConnection conn = new ServiceConnection((List<String>) details.get("categories"),
				(String) details.get("ip"));
		activeConnections.add(conn);
		System.out.println(conn.getSubscribedCategories());
		System.out.println(conn.getIp());
	}

}
