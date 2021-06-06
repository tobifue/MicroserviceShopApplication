package ase.gateway.controller.heartbeat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ase.gateway.controller.TrafficController;
import ase.gateway.traffic.Message;
import ase.gateway.traffic.ServiceConnection;

public final class HeartbeatMonitor {

	public static String printCurrentHeartbeatStatus(List<ServiceConnection> activeConnections) {
		StringBuilder sb = new StringBuilder();
		sb.append("Heartbeats:\n");
		for (HeartbeatStatus hb : queryHeartbeats(activeConnections)) {
			sb.append(String.format("[ip: %s / category: %s / status: %s]\n", hb.getConnection().getIp(),
					hb.getConnection().getCategory(), hb.getStatus()));
		}
		return sb.toString();
	}

	public static List<HeartbeatStatus> queryHeartbeats(List<ServiceConnection> activeConnections) {
		List<HeartbeatStatus> heartbeats = new ArrayList<HeartbeatStatus>();
		for (ServiceConnection conn : activeConnections) {
			try {
				if (TrafficController.dispatch(conn, Message.createInstance(null, "heartbeat", "/heartbeat", "GET"))
						.equals("OK")) {
					heartbeats.add(new HeartbeatStatus(conn, "ALIVE"));
				} else
					heartbeats.add(new HeartbeatStatus(conn, "DEAD"));
			} catch (RestClientException e) {
				heartbeats.add(new HeartbeatStatus(conn, "DEAD"));
			}

		}
		return heartbeats;
	}
}
