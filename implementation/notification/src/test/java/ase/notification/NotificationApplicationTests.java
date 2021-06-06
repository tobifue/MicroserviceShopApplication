package ase.notification;

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

import ase.notification.data.Notification;
import ase.notification.data.NotificationRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class NotificationApplicationTests {

	private MockMvc mvc;

	@MockBean
	private NotificationRepository repository;

	@Autowired
	private WebApplicationContext context;

	private Notification notification;

	@BeforeAll
	public static void setup() {

	}

	@BeforeEach
	public void initialize() {
		this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
		notification = new Notification(1L, 4, "test", "a01528726@unet.univie.ac.at");
	}

	@Test
	@DisplayName("Store Notification")
	public void storeShipment() throws Exception {
		mvc.perform(
				post("/add").content(asJsonString(notification)).contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Store Incorrect Notification")
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
