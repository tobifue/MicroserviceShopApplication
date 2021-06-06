package ase.gateway.controller.heartbeat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ase.gateway.controller.TrafficController;
import ase.gateway.traffic.Message;
import ase.gateway.traffic.ServiceConnection;

public final class HeartbeatMonitor {

	private final Thread monitorThread;
	public List<ServiceConnection> activeConnections = new ArrayList<>();
	public static List<HeartbeatStatus> heartbeats = new ArrayList<>();

	public HeartbeatMonitor() {
		monitorThread = new Thread(() -> {
			while (true) {
				queryHeartbeats();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(printCurrentHeartbeatStatus());
			}
		});
		monitorThread.start();
	}

	public String printCurrentHeartbeatStatus() {
		StringBuilder sb = new StringBuilder();
		System.out.println("Heartbeats:");
		for (HeartbeatStatus hb : getHeartbeats()) {
			sb.append(String.format("[ip: %s / category: %s / status: %s]\n", hb.getConnection().getIp(),
					hb.getConnection().getCategory(), hb.getStatus()));
		}
		return sb.toString();
	}

	private synchronized void queryHeartbeats() {
		List<HeartbeatStatus> heartbeatsTemp = new ArrayList<>();
		List<ServiceConnection> connectionsTemp = getConnections();
		for (ServiceConnection conn : connectionsTemp) {
			try {
				if (TrafficController.dispatch(conn, Message.createInstance(null, "heartbeat", "/heartbeat", "GET"))
						.equals("OK")) {
					heartbeatsTemp.add(new HeartbeatStatus(conn, "ALIVE"));
				} else
					heartbeatsTemp.add(new HeartbeatStatus(conn, "DEAD"));
			} catch (RestClientException e) {
				heartbeatsTemp.add(new HeartbeatStatus(conn, "DEAD"));
			}
		}
		heartbeats = heartbeatsTemp;
	}

	public synchronized void updateConnections(List<ServiceConnection> connections) {
		this.activeConnections = connections;
	}

	private synchronized List<ServiceConnection> getConnections() {
		return activeConnections;
	}

	public static synchronized List<HeartbeatStatus> getHeartbeats() {
		return heartbeats;
	}

	public class HeartbeatStatus {
		private final ServiceConnection connection;
		private final String status;

		public HeartbeatStatus(ServiceConnection connection, String status) {
			this.connection = connection;
			this.status = status;
		}

		public ServiceConnection getConnection() {
			return connection;
		}

		public String getStatus() {
			return status;
		}
	}

}
