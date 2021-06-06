package ase.gateway.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ase.gateway.controller.heartbeat.HeartbeatMonitor;
import ase.gateway.traffic.Message;
import ase.gateway.traffic.ServiceConnection;
import ase.gateway.util.NetworkUtil;

@RestController
@RequestMapping("register")
public class TrafficController {

	private static List<ServiceConnection> activeConnections = Collections
			.synchronizedList(new ArrayList<ServiceConnection>());
	private static Thread monitorThread;

	/**
	 * @param registration details in format { "endpoints" : ["/test",
	 *                     "/test/{id}"],"category": "inventory", "ip":
	 *                     "200.0.0.199:8030" }
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(path = "/new", consumes = "application/json", produces = "application/json")
	public void registerNewService(@RequestBody Map<String, Object> details) {
		boolean newConnection = true;
		for (ServiceConnection c : activeConnections) {
			if (c.getIp().equals(details.get("ip")))
				newConnection = false;
		}
		if (newConnection) {
			ServiceConnection conn = new ServiceConnection((List<String>) details.get("endpoints"),
					(String) details.get("category"), (String) details.get("ip"));
			activeConnections.add(conn);
			System.out.println(conn.getSubscribedEndpoints());
			System.out.println(conn.getCategory());
			System.out.println(conn.getIp());
		}
	}

	public static String sendMessageToSingleRecipient(Message message) {
		return dispatch(getSubscribedConnections(message.getCategory(), message.getEndpoint()).get(0), message);
	}

	public static String dispatch(ServiceConnection connection, Message message) {
		switch (message.getHttpMethod()) {
		case "POST":
			return NetworkUtil.httpPost(connection.getIp(), message.getEndpoint(), message.getPayload());
		default:
		case "GET":
			return NetworkUtil.httpGet(connection.getIp(), message.getEndpoint());
		}
	}

	public static List<ServiceConnection> getAllActiveConnections() {
		return activeConnections;
	}

	public static List<ServiceConnection> getSubscribedConnections(String category, String endpoint) {
		List<ServiceConnection> result = new ArrayList<ServiceConnection>();
		for (ServiceConnection s : activeConnections) {
			if (s.getCategory().equals(category)) {
				for (String ep : s.getSubscribedEndpoints()) {
					// this way the service can subscribe to endpoints that contain pathvariables
					if (endpoint.contains(ep))
						result.add(s);
				}
			}
		}
		return result;
	}

	@Bean
	public CommandLineRunner injectMonitorThread() {
		return (args) -> {
			monitorThread = new Thread(() -> {
				while (true) {
					System.out.println(HeartbeatMonitor.printCurrentHeartbeatStatus(activeConnections));
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		};
	}

}
