package ase.gateway.controller;

import ase.gateway.util.AdressUtil;
import ase.gateway.util.NetworkUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("checkout")
public class CheckoutController {
    private static final String serviceName = "checkout-service";


    @GetMapping("/checkout/{costumerId}")
    public String checkout(@PathVariable Long costumerId) {
        System.out.println("/checkout in CheckoutController is called with ID: " + costumerId);
        try {
            String cart = NetworkUtil.httpGet(AdressUtil.loadAdress("cart-service"), "/" + costumerId);
            Map<String, Object> result = new ObjectMapper().readValue(cart, HashMap.class);
            return NetworkUtil.httpPost(AdressUtil.loadAdress(serviceName), "/" + costumerId, result);
        } catch (RestClientException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
