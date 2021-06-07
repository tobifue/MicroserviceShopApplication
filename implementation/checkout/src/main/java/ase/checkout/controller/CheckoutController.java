package ase.checkout.controller;

import ase.checkout.util.NetworkUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
public class CheckoutController {
    private String userId;
    private String jsonCart;
    private String gatewayIp = "http://" + (System.getenv("GATEWAYIP") == null ? "localhost" : System.getenv("GATEWAYIP")) + ":8080";
    private String checkoutAdress = "http://" + InetAddress.getLocalHost().getHostAddress() + ":8086";

    @RequestMapping(value = "/checkout/{costumerId}", method = RequestMethod.GET)
    public String checkout(@PathVariable("costumerId") Long costumerId) {
        String cart = NetworkUtil.httpGet(gatewayIp, String.format("/cart/getCart/%s", costumerId));
        NetworkUtil.httpGet(gatewayIp, String.format("/cart/deleteCart/%s", costumerId));
        String user = NetworkUtil.httpGet(gatewayIp, String.format("/users/%s", costumerId));

        System.out.println("cart " + cart);
        System.out.println("user " + user);

        try {
            JSONObject jsonObject = new JSONObject(cart);
            JSONObject jsonUserObject = new JSONObject(user);

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            System.out.println(jsonArray); // display usernames
            System.out.println(jsonUserObject.getString("email")); // display usernames


            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> item = new ObjectMapper().readValue(jsonArray.getJSONObject(i).toString(), HashMap.class);

                item.put("customerId", costumerId.toString());
                //item.put("vendorId", "2");
                int bought_quantity = Integer.parseInt(item.get("quantity").toString());
                String prev_quantity = NetworkUtil.httpGet(gatewayIp, String.format("/inventory/%s", item.get("itemId")));
                JSONObject jsonItemsObject = new JSONObject(prev_quantity);
                int remain_quantity = Integer.parseInt(jsonItemsObject.getString("quantity")) - bought_quantity;

                //substract bought volume from quantity
                item.put("quantity", remain_quantity);

                //update item, if quantity is empty delete
                if (remain_quantity < 1) {
                    NetworkUtil.httpPost(gatewayIp, String.format("inventory/delete/%s", item.get("itemId")), item);
                } else {
                    NetworkUtil.httpPost(gatewayIp, String.format("inventory/update/%s", item.get("itemId")), item);
                }
                //get User mail
                System.out.println("usernames" + user); // display usernames
                System.out.println("itemId" + item.get("itemId"));
                NetworkUtil.httpPost(gatewayIp, "history/add", item);
                item.put("email", jsonUserObject.getString("email"));
                System.out.println(item); // display usernames
                NetworkUtil.httpPost(gatewayIp, "shipment/add", item);
            }
            System.out.println("cart: " + cart);
        } catch (RestClientException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (JSONException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "{ \"command\":\"delete\"}";
    }

    @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
    @ResponseBody
    public String heartbeat() {
        return "OK";
    }

    public CheckoutController() throws UnknownHostException {
        new Thread(() -> {
            while (!registerWithGateway()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @RequestMapping(value = "/registerWithGateway", method = RequestMethod.GET)
    private boolean registerWithGateway() {
        try {
            Map<String, Object> registrationDetails = new HashMap<>();
            registrationDetails.put("endpoints", new ArrayList<String>() {
                private static final long serialVersionUID = 1L;

                {
                    add("/checkout");
                }
            });
            registrationDetails.put("category", "checkout");
            registrationDetails.put("ip", checkoutAdress);
            new RestTemplate().postForObject(String.format("%s/%s", gatewayIp, "/register/new"),
                    registrationDetails, String.class);
            return true;
        } catch (RestClientException e) {
            System.err.println("Failed to connect to Gateway, please register manually or restart application");
            return false;
        }

    }
}