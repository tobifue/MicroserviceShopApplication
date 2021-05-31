package com.tobi.department;

import com.tobi.department.entity.Item;
import com.tobi.department.entity.ItemFactory;
import com.tobi.department.repository.InventoryRepository;
import com.tobi.department.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import static org.hamcrest.MatcherAssert.assertThat;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@ContextConfiguration(classes = AccessControlApplication.class)
@SpringBootTest
public class InventoryIntegrationTest {
/*
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Item item;// = new Item(2.0,2,4,"test",2.0);
    private Item itemuno;
    private Item item2;

    @Autowired
    private InventoryRepository repository;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void initialize() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        item2 = ItemFactory.createInstance(38L, 48.9, 30, 17, "Testitem", 3.5);
        itemuno = ItemFactory.createInstance(39L, 48.9, 30, 17, "Testitem", 3.5);
    }

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testApplication() {
        int resourceId = 35;
        ResponseEntity<Item> response = testRestTemplate.getForEntity("/inventory/35", Item.class, 35L);
        //assertThat(response).isNotNull();
        //assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Item body = response.getBody();
        System.out.println(body);
    }
*/
    @Autowired
    private InventoryRepository repository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getItemsTest() throws Exception {

        //why can i not save a new Item?
        Item test = new Item(104L, 48.9, 30, 17, "Testitem", 3.5);
        repository.save(test);
        System.out.println(repository.findByItemId(1L));


        List<Item> testList = new ArrayList<Item>();

        //Mockito.when(inventoryService.findAllItems()).thenReturn(testList);
        mockMvc.perform(MockMvcRequestBuilders.get("/inventory/1"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("{\"itemId\":1,\"price\":48.9,\"vendorId\":30,\"quantity\":17,\"itemName\":\"Testitem\",\"priceRecommendation\":3.5}"));
    }

}
