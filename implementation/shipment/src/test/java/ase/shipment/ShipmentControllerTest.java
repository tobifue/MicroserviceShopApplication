package ase.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import ase.shipment.controller.ShipmentController;
import ase.shipment.data.Shipment;
import ase.shipment.data.Shipment.ShippingStatus;
import ase.shipment.data.ShipmentRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ShipmentControllerTest {

	private MockMvc mvc;

	@MockBean
	private ShipmentRepository repository;

	@Autowired
	private WebApplicationContext context;

	private ShipmentController controller;
	private Shipment shipment;

	@BeforeAll
	public static void setup() {

	}

	@BeforeEach
	public void initialize() {
		controller = new ShipmentController(repository);
		this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
		shipment = new Shipment(1L, 2L, 3L, "email", 10, "testitem", 1);
	}

	@Test
	@DisplayName("Full Shipment Process")
	public void updateShipment() {
		assertEquals(shipment.getShippingStatus(), ShippingStatus.ORDER_RECEIVED.getTitle());
		controller.updateShipment(shipment);
		assertEquals(shipment.getShippingStatus(), ShippingStatus.IN_DISTR_CENTER.getTitle());
		controller.updateShipment(shipment);
		assertEquals(shipment.getShippingStatus(), ShippingStatus.IN_DELIVERY.getTitle());
		controller.updateShipment(shipment);
		assertEquals(shipment.getShippingStatus(), ShippingStatus.DELIVERED.getTitle());
	}

	@Test
	@DisplayName("Store Shipment")
	public void storeShipment() throws Exception {
		mvc.perform(post("/add").content(asJsonString(shipment)).contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Store Incorrect Shipment")
	public void storeWrongShipment() throws Exception {
		mvc.perform(post("/add").content("WRONG XXXXX").contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	// http://www.masterspringboot.com/getting-started/testing-spring-boot/testing-spring-boot-applications-with-mockmvc
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
