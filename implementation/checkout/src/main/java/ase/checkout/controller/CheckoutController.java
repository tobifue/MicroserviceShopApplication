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
    private String gatewayIp = "http://localhost:8080";
    private String checkoutAdress = "http://" + InetAddress.getLocalHost().getHostAddress() + ":8086";

    @RequestMapping(value = "/checkout/{costumerId}", method = RequestMethod.GET)
    public String checkout(@PathVariable("costumerId") Long costumerId) {
        /*try {
            //ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //String json = ow.writeValueAsString(cart);
            //jsonCart = json;

            System.out.println(costumerId);
        } catch (Exception e) {
            System.out.println("Something went wrong.");
        }*/
        String cart = NetworkUtil.httpGet(gatewayIp, String.format("/cart/getCart/%s", costumerId));
        NetworkUtil.httpGet(gatewayIp, String.format("/cart/deleteCart/%s", costumerId));
        try {
            JSONObject jsonObject = new JSONObject(cart);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> item = new ObjectMapper().readValue(jsonArray.getJSONObject(i).toString(), HashMap.class);
                item.put("vendorId", costumerId.toString());
                System.out.println(item); // display usernames
                NetworkUtil.httpPost(gatewayIp, String.format("history/add", costumerId), item);
                NetworkUtil.httpPost(gatewayIp, String.format("shipment/add", costumerId), item);
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
        registerWithGateway();
    }

    @RequestMapping(value = "/registerWithGateway", method = RequestMethod.GET)
    private void registerWithGateway() {
        try {
            Map<String, Object> registrationDetails = new HashMap<>();
            registrationDetails.put("endpoints", new ArrayList<String>() {
                private static final long serialVersionUID = 1L;

                {
                    // put highest level endpoints here
                    add("/checkout");
                }
            });
            registrationDetails.put("category", "checkout");
            registrationDetails.put("ip", checkoutAdress);
            new RestTemplate().postForObject(String.format("%s/%s", gatewayIp, "/register/new"),
                    registrationDetails, String.class);
            System.out.println("Successfully registered with gateway!");
        } catch (RestClientException e) {
            System.err.println("Failed to connect to Gateway, please register manually or restart application");
        }
    }


}
