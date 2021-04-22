package ase.gateway.traffic;

import java.util.Map;

public class Message {

	// captures network event
	private final Map<String, Object> payload;
	private final String category;
	private final String destination; // ServiceConnection
	private final String httpMethod;

	private Message(Map<String, Object> payload, String category, String destination, String httpMethod) {
		this.payload = payload;
		this.category = category;
		this.destination = destination;
		this.httpMethod = httpMethod;
	}

	public Message createInstance(Map<String, Object> payload, String category, String destination, String httpMethod) {
		// TODO validate parameters
		return new Message(payload, category, destination, httpMethod);
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

	public String getCategory() {
		return category;
	}

	public String getDestination() {
		return destination;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

}
