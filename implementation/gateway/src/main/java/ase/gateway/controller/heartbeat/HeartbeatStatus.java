package ase.gateway.controller.heartbeat;

import ase.gateway.traffic.ServiceConnection;

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