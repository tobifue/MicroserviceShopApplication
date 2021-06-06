package com.tobi.department;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobi.department.entity.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.tobi.department.repository.InventoryRepository;
import com.tobi.department.entity.ItemFactory;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {
/*
	private MockMvc mvc;

	@MockBean
	private InventoryRepository repository;

	@Autowired
	private WebApplicationContext context;

	private Item item;

	@BeforeAll
	public static void setup() {

	}

	@BeforeEach
	public void initialize() {
		this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
		item = ItemFactory.createInstance(38L, 48.9, 30, 17, "Testitem",3.5);
	}

	@Test
	@DisplayName("Add Item")
	public void addItem() throws Exception {
		mvc.perform(
				post("/").content(asJsonString(item)).contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Add Wrong Item")
	public void addWrongItem() throws Exception {
		mvc.perform(post("/").content("WRONG XXXXX").contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
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
*/
}
