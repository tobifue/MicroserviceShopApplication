package ase.gateway.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import ase.gateway.util.AdressUtil;
import ase.gateway.util.NetworkUtil;


@RestController
@RequestMapping("cart")
public class CartController {
    private static final String serviceName = "cart";

    /**
     * @param transaction in format { "buyerId" : "1", "sellerId": "2", "price":
     *                    "10", "itemTitle": "nice product", "count" : "1" }
     * @return the transaction
     */
    @PostMapping(path = "/addItemToCart/{costumerId}", consumes = "application/json", produces = "application/json")

    public String addItemToCart(@RequestBody Map<String, Object> transaction) {
        System.out.println("cart Controller is called with" + transaction.toString());
        String outputJson = "{ \"command\":\"delete\"}";
        return outputJson;
    }


}
