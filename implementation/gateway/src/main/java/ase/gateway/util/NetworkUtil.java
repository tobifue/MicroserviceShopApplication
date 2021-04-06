package ase.gateway.util;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public final class NetworkUtil {

	/**
	 * 
	 * @param serviceAddress the target ip and port with format 1.1.1.1:8080
	 * @param endpoint       the endpoint without "/"
	 * @return the response as String
	 * 
	 * @throws RestClientException
	 */
	public static String httpGet(String serviceAddress, String endpoint) throws RestClientException {
		return new RestTemplate().getForObject(String.format("%s/%s", serviceAddress, endpoint), String.class);
	}

	/**
	 * 
	 * @param serviceAddress the target ip and port with format 1.1.1.1:8080
	 * @param endpoint       the endpoint without "/"
	 * @param message        the message body, typically a json parsed as string
	 * @return the response as String
	 * 
	 * @throws RestClientException
	 */
	public static String httpPost(String serviceAddress, String endpoint, String message) throws RestClientException {
		return new RestTemplate().postForObject(String.format("%s/%s", serviceAddress, endpoint), message,
				String.class);
	}

	private NetworkUtil() {
		// prevent instance
	}
}
