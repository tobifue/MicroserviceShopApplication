package ase.checkout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.web.bind.annotation.*;

import ase.checkout.itemhandler.*;


@RestController
public class CheckoutController {
    private String userId;
    private String jsonCart;


    @GetMapping("/checkout/{costumerId}")
    public String checkout(@PathVariable("costumerId") Long costumerId) {
        System.out.println("lolo");
        try {
            /*ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(cart);
            jsonCart = json;*/
            System.out.println(costumerId);
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
