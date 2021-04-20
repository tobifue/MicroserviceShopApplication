package ase.gateway.controller;

/*
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
*/