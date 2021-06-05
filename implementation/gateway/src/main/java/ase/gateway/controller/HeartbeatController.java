package ase.gateway.controller;

import ase.gateway.controller.heartbeat.HeartbeatMonitor;
import ase.gateway.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("heartbeat")
public class HeartbeatController {

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getHeartBeatStatus() throws JSONException {
        List<HeartbeatMonitor.HeartbeatStatus> conn_list = HeartbeatMonitor.getHeartbeats();
        JSONArray jsonArray = new JSONArray();

        for (int i =0; i < conn_list.size(); i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("category", conn_list.get(i).getConnection().getCategory());
            jsonObject.put("ip", conn_list.get(i).getConnection().getIp());
            jsonObject.put("status", conn_list.get(i).getStatus());
            jsonArray.put(jsonObject);
        }

        return jsonArray.toString();
    }

}
