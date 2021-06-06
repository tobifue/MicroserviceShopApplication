package com.tobi.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tobi.user.entity.Item;
import com.tobi.user.service.AccountService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RestTemplate restTemplate;
    /**
     * GET method endpoint to get profit by vendorID. Returns
     * json with plain double value.
     * @param vendorId id of the vendor in Long format
     * @return String json with list of items
     */
    @SneakyThrows
    @GetMapping(path = "/vendor/{id}", produces="application/json")
    public String getProfitByVendorId(@PathVariable("id") Long vendorId){
        String json = "";
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            json = ow.writeValueAsString(accountService.getItemsByVendorId(vendorId));
        } catch (Exception e) {
            System.out.println("Something went wrong.");
        }
        return json;
    }

    @Value("${server.port}")
    private String port;

    /**
     * Endpoint to register service with gateway service.
     * Registration details (port, category) are set.
     * Here i am.
     */
    @RequestMapping(value = "/registerWithGateway", method = RequestMethod.GET)
    private boolean registerWithGateway() {
        try {
            Map<String, Object> registrationDetails = new HashMap<>();
            registrationDetails.put("endpoints", new ArrayList<String>() {
                private static final long serialVersionUID = 1L;
                {
                    // put highest level endpoints here
                    add("/vendor");
                }
            });
            String checkoutAdress = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port;
            String gatewayIp = "http://" + (System.getenv("GATEWAYIP") == null ? "localhost" : System.getenv("GATEWAYIP")) + ":8080";
            registrationDetails.put("category", "account");
            registrationDetails.put("ip", checkoutAdress);
            new RestTemplate().postForObject(String.format("%s/%s", gatewayIp, "/register/new"),
                    registrationDetails, String.class);
            System.out.println("Successfully registered with gateway!");
            return true;
        } catch (RestClientException | UnknownHostException e) {
            System.err.println("Failed to connect to Gateway, please register manually or restart application");
            return false;
        }
    }

    /**
     * Endpoint to call for gateway to check
     * if service is alive.
     * @return String "OK"
     */
    @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
    @ResponseBody
    public String heartbeat() {
        return "OK";
    }

    /**
     * Try to register with gateway.
     */
    @Bean
    public CommandLineRunner registerWithGateWay() {
        return (args) -> {
            // register with gateway in commandlineRunner
            registerWithGateway();
        };
    }

    /**
     * If registerWithGateway was not successful,
     * continuously try again.
     */
    @Bean
    public CommandLineRunner registerAgainWithGateway( ) {
        return (args) -> {
            new Thread(() -> {
                while (!registerWithGateway()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        };
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
