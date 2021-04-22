package ase.gateway.traffic;

import java.util.Map;

public class Message {

	// captures network event
	private final Map<String, Object> payload;
	private final String category;
	private final String endpoint; // ServiceConnection
	private final String httpMethod;

	private Message(Map<String, Object> payload, String category, String endpoint, String httpMethod) {
		this.payload = payload;
		this.category = category;
		this.endpoint = endpoint;
		this.httpMethod = httpMethod;
	}

	public static Message createInstance(Map<String, Object> payload, String category, String endpoint,
			String httpMethod) {
		// TODO validate parameters
		return new Message(payload, category, endpoint, httpMethod);
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

	public String getCategory() {
		return category;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

}
