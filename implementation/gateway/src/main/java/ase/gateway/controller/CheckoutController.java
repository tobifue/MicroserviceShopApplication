package ase.gateway.controller;

import ase.gateway.GatewayApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/*
@RestController
public class CheckoutController {
    private String userId;
    private String jsonCart;
    @PostMapping(path = "/checkout", consumes = "application/json", produces = "application/json")
    public String checkout(@RequestBody GatewayApplication.Cart cart) {
        try {
            ObjectWriter ow = (ObjectWriter) new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(cart);
            jsonCart = json;
        }catch (Exception e){
            System.out.println("Something went wrong.");
        }
        return "Customer information saved successfully ::." + cart.getCostumerId() + " " + cart.getPrice() ;
    }

    public void doShipment(){	}
    public void saveTransactionInDb(){	}
}

*/
