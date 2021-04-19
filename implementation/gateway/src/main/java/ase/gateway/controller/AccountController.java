package ase.gateway.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import ase.gateway.util.AdressUtil;
import ase.gateway.util.NetworkUtil;

@RestController
@RequestMapping("account")
public class AccountController {

    private static final String serviceName = "account-service";

    @GetMapping("/vendor/{id}")
    public String getProfitByVendorId(@PathVariable("id") Long vendorId){
        try {
            return NetworkUtil.httpGet(AdressUtil.loadAdress(serviceName), String.format("%d", vendorId));
        } catch (RestClientException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
