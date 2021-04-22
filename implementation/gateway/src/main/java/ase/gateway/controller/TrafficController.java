package ase.gateway.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ase.gateway.traffic.Message;
import ase.gateway.traffic.ServiceConnection;
import ase.gateway.util.NetworkUtil;

@RestController
@RequestMapping("register")
public class TrafficController {

	private static List<ServiceConnection> activeConnections = new ArrayList<>();
	// TODO buffer messages if passthrough fails

	/**
	 * @param registration details in format { "endpoints" : ["/test",
	 *                     "/test/{id}"],"category": "inventory", "ip":
	 *                     "200.0.0.199:8030" }
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(path = "/new", consumes = "application/json", produces = "application/json")
	public void registerNewService(@RequestBody Map<String, Object> details) {
		ServiceConnection conn = new ServiceConnection((List<String>) details.get("endpoints"),
				(String) details.get("category"), (String) details.get("ip"));
		activeConnections.add(conn);
		System.out.println(conn.getSubscribedEndpoints());
		System.out.println(conn.getIp());
	}

	public static String sendMessageToSingleRecipient(Message message) {
		// TODO check if successful, if not buffer message and try again
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

	public static List<ServiceConnection> getSubscribedConnections(String category, String endpoint) {
		List<ServiceConnection> result = new ArrayList<ServiceConnection>();
		for (ServiceConnection s : activeConnections) {
			if (s.getCategory().equals(category) && s.getSubscribedEndpoints().contains(endpoint))
				result.add(s);
		}
		return result;
	}

}
