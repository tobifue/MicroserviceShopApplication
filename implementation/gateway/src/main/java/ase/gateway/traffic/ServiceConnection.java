package ase.gateway.traffic;

import java.util.List;

public class ServiceConnection {

	private final List<String> subscribedCategories;
	private final String ip;

	public ServiceConnection(List<String> subscribedCategories, String ip) {
		this.subscribedCategories = subscribedCategories;
		this.ip = ip;
	}

	public List<String> getSubscribedCategories() {
		return subscribedCategories;
	}

	public String getIp() {
		return ip;
	}

}