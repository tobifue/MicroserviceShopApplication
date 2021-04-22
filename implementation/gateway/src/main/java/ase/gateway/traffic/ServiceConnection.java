package ase.gateway.traffic;

import java.util.List;

public class ServiceConnection {

	private final List<String> subscribedEndpoints;
	private final String category; // equals the requestmapping of the respective controller
	private final String ip;

	public ServiceConnection(List<String> subscribedEndpoints, String category, String ip) {
		this.subscribedEndpoints = subscribedEndpoints;
		this.category = category;
		this.ip = ip;
	}

	public List<String> getSubscribedEndpoints() {
		return subscribedEndpoints;
	}

	public String getCategory() {
		return category;
	}

	public String getIp() {
		return ip;
	}

}