package ase.markedproduct;

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

import ase.markedproduct.controller.UpdateController;
import ase.markedproduct.data.MarkedProduct;
import ase.markedproduct.data.MarkedProductRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MarkedProductControllerTest {

	private MockMvc mvc;

	@MockBean
	private MarkedProductRepository repository;

	@Autowired
	private WebApplicationContext context;

	private UpdateController controller;
	private MarkedProduct markedproduct;

	@BeforeAll
	public static void setup() {

	}

	@BeforeEach
	public void initialize() {
		controller = new UpdateController();
		this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
		markedproduct = new MarkedProduct(111L, 555L, 842.7, "iPhone XS", "max.muller@gmail.com");
	}

	@Test
	@DisplayName("Mark Product")
	public void markProduct() throws Exception {
		mvc.perform(post("/mark").content(asJsonString(markedproduct)).contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Mark Product Incorrectly")
	public void storeWrongShipment() throws Exception {
		mvc.perform(post("/mark").content("Random String").contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
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
