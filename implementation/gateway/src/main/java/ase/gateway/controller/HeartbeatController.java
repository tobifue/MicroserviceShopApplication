package ase.gateway.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ase.gateway.controller.heartbeat.HeartbeatMonitor;
import ase.gateway.controller.heartbeat.HeartbeatStatus;

@RestController
@RequestMapping("heartbeat")
public class HeartbeatController {

	@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getHeartBeatStatus() throws JSONException {
		List<HeartbeatStatus> heartbeats = HeartbeatMonitor.queryHeartbeats(TrafficController.getAllActiveConnections());
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < heartbeats.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("category", heartbeats.get(i).getConnection().getCategory());
			jsonObject.put("ip", heartbeats.get(i).getConnection().getIp());
			jsonObject.put("status", heartbeats.get(i).getStatus());
			jsonArray.put(jsonObject);
		}

		return jsonArray.toString();
	}

}
