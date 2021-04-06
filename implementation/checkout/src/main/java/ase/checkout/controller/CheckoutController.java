package ase.checkout.controller;

import ase.gateway.GatewayApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ase.checkout.itemHandler.*;


@RestController
public class CheckoutController {
    private String userId;
    private String jsonCart;

    @PostMapping(path = "/checkout", consumes = "application/json", produces = "application/json")
    public String checkout(@RequestBody Cart cart) {
        try {
            ObjectWriter ow = (ObjectWriter) new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(cart);
            jsonCart = json;
        } catch (Exception e) {
            System.out.println("Something went wrong.");
        }
        String outputJson = "{ \"command\":\"delete\"}";
        return outputJson;
    }

    public void doShipment() {
    }

    public void saveTransactionInDb() {
    }
}
