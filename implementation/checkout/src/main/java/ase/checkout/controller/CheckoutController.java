package ase.checkout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import ase.checkout.itemhandler.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
public class CheckoutController {
    private String userId;
    private String jsonCart;

    @Value("${server.port}")
    private String port = "8086";

    @RequestMapping(value = "/checkout/{costumerId}", method = RequestMethod.GET)
    public String checkout(@PathVariable("costumerId") Long costumerId, Map<String, Object> cart) {
        System.out.println("lolo");
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(cart);
            jsonCart = json;

            System.out.println(costumerId);
        } catch (Exception e) {
            System.out.println("Something went wrong.");
        }
        String outputJson = "{ \"command\":\"delete\"}";
        doShipment();
        saveTransactionInDb();
        return outputJson;
    }

    public void doShipment() {
    }

    public void saveTransactionInDb() {
    }
    public CheckoutController (){
        registerWithGateway();
    }
    //public CheckoutController(){registerWithGateway();}
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
            registrationDetails.put("ip", "http://localhost:" + port);
            new RestTemplate().postForObject(String.format("%s/%s", "http://localhost:8080", "/register/new"),
                    registrationDetails, String.class);
        } catch (RestClientException e) {
            System.out.println("Could not reach Gateway, retrying in 5 seconds");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            registerWithGateway();
        }
        System.out.println("Successfully registered with gateway!");
    }



}
