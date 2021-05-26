package ase.checkout;


import ase.checkout.controller.CheckoutController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckoutControllerTest {
    private MockMvc mvc;


    @Autowired
    private WebApplicationContext context;

    private CheckoutController controller;


    @BeforeAll
    public static void setup() {

    }

    @BeforeEach
    public void initialize() throws UnknownHostException {
        controller = new CheckoutController();
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    @DisplayName("checkout")
    public void updateShipment() {
        long costumerId = 0;
        assertEquals(controller.checkout(costumerId), "{ \"command\":\"delete\"}");
    }
}
