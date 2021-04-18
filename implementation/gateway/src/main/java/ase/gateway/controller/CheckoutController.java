package ase.gateway.controller;

import ase.gateway.util.AdressUtil;
import ase.gateway.util.NetworkUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("checkout")
public class CheckoutController {
    private static final String serviceName = "checkout";


    /**
     * @param
     * @return ists all transactions where the user was involved in
     */
    @RequestMapping(value = "/checkout/{userid}", method = RequestMethod.GET)
    @ResponseBody
    public String checkout(@PathVariable Long userid) {
        System.out.println("/checkout in CheckoutController is called with ID: " + userid);
        String outputJson = "{ \"command\":\"ToDo\"}";
        return outputJson;
    }
}
