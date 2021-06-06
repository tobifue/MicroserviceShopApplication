package com.tobi.department.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tobi.department.entity.Item;
import com.tobi.department.service.InventoryService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Controller class for inventory service.
 */
@RestController
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * Saves an Item sent in body via Repository to database.
     * @param inventory of type Item
     * @return jsonified created Item
     */
    @SneakyThrows
    @PostMapping(path ="/", consumes = "application/json", produces = "application/json")
    public String saveItem(@RequestBody Item inventory){
    try {
        if (inventory.getVendorId() == null || inventory.getItemName() == null || inventory.getQuantity() == null || inventory.getPrice() == null) {
            return "Not all input is set! Check for: vendorId, itemName, quantity and price!";
        } else {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(inventoryService.saveItem(inventory));

            return json;
        }
    }
    catch(Exception e){
        e.printStackTrace();
        return e.getMessage();
    }
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such Order")  // 404
    public class OrderNotFoundException extends RuntimeException {
    }
    //get Item
    @SneakyThrows
    @GetMapping(path = "/{id}", produces = "application/json")
    public String findByItemId(@PathVariable("id")Long itemId){
    try {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(inventoryService.findByItemId(itemId));

        if (inventoryService.findByItemId(itemId) == null) throw new ItemNotFoundException("item with this id not found");

        return json;
    }
    catch(ItemNotFoundException e){
        e.printStackTrace();
        return e.getMessage();
    }
    }

    //get List of all Items from Vendor
    @SneakyThrows
    @RequestMapping(value="/vendor/{id}")
    @GetMapping(produces = "application/json")
    public @ResponseBody String findAllObjects(@PathVariable("id")Integer id) {

    try {
        List<Item> inventories = new ArrayList<Item>();
        List<Item> v_inventories = new ArrayList<Item>();

        inventories = inventoryService.findAllItems();


        for (Item dep : inventories) {
            if (dep.getVendorId() == id) {
                v_inventories.add(dep);
            } else {
                //nothing to do here
            }
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(v_inventories);

        return json;
    }
    catch(Exception e){
        e.printStackTrace();
        return e.getMessage();
    }
    }

    @SneakyThrows
    @RequestMapping(value="/items/")
    @GetMapping(produces = "application/json")
    public @ResponseBody String findAllItems() throws JsonProcessingException {
    try {
        List<Item> itemss = new ArrayList<Item>();

        itemss = inventoryService.findAllItems();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(itemss);

        return json;
    }
    catch(Exception e){
        e.printStackTrace();
        return e.getMessage();
    }
    }

    @SneakyThrows
    @RequestMapping(value="/vendor")
    @GetMapping(consumes="application/json", produces ="application/json")
    public @ResponseBody String findAllObjects() {
    try {
        List<Item> inventories = new ArrayList<Item>();

        inventories = inventoryService.findAllItems();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(inventories);

        return json;
    }
    catch(Exception e){
        e.printStackTrace();
        return e.getMessage();
    }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    //update Item
    @SneakyThrows
    @RequestMapping(value = "/update/{id}", consumes = "application/json", produces="application/json")
    @PostMapping
    public String update(@PathVariable("id") Long departmentId, @RequestBody Item update_item) throws JsonProcessingException {
    try {
        Item inventory = inventoryService.findByItemId(departmentId);

        if (update_item.getVendorId() != null) {
            inventory.setVendorId(update_item.getVendorId());
        }
        if (update_item.getQuantity() != null) {
            inventory.setQuantity(update_item.getQuantity());
        }
        if (update_item.getItemName() != null) {
            inventory.setItemName(update_item.getItemName());
        }
        if (update_item.getPrice() != null) {
            inventory.setPrice(update_item.getPrice());
        }
        //code
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, Object> map = new HashMap<>();
        map.put("itemName", inventory.getItemName());
        map.put("vendorId", inventory.getVendorId().toString());
        map.put("price", inventory.getPrice().toString());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        String gatewayIp = "http://" + (System.getenv("GATEWAYIP") == null ? "localhost" : System.getenv("GATEWAYIP")) + ":8080";

        System.out.print(entity);
        ResponseEntity<String> response = restTemplate.postForEntity(gatewayIp + "/markedproduct/update", entity, String.class);
        System.out.print(response);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(inventoryService.saveItem(inventory));

        return json;
    }
    catch(Exception e){
        e.printStackTrace();
        return e.getMessage();
    }
    }

    //delete Item
    @RequestMapping(value = "/delete/{id}")
    @PostMapping
    public String delete(@PathVariable("id") Long departmentId) {
    try {
        inventoryService.deleteByItemId(departmentId);
        return "Delete successfull";
    }
    catch(Exception e){
        e.printStackTrace();
        return e.getMessage();
    }
    }

    @Value("${server.port}")
    private String port;

    @RequestMapping(value = "/registerWithGateway", method = RequestMethod.GET)
    private boolean registerWithGateway() {
        try {
            Map<String, Object> registrationDetails = new HashMap<>();
            registrationDetails.put("endpoints", new ArrayList<String>() {
                private static final long serialVersionUID = 1L;
                {
                    // put highest level endpoints here
                    add("/");
                    add("/update");
                    add("/delete");
                    add("/vendor");
                    add("/items/");
                }
            });
            String checkoutAdress = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port;
            String gatewayIp = "http://" + (System.getenv("GATEWAYIP") == null ? "localhost" : System.getenv("GATEWAYIP")) + ":8080";
            registrationDetails.put("category", "inventory");
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

    @Bean
    public CommandLineRunner continuousRegistrationWithGateway() {
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

    @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
    @ResponseBody
    public String heartbeat() {
        return "OK";
    }

    @Bean
    public CommandLineRunner registerWithGateWay() {
        return (args) -> {
            // register with gateway in commandlineRunner
            registerWithGateway();
        };
    }
}
