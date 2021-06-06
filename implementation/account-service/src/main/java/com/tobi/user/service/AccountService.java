package com.tobi.user.service;

import com.tobi.user.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Item class to manage retrieved list of items
 * when calling txhistory endpoint in AccountService.
 */
@Service
@Slf4j
public class AccountService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Calls txhistory endpoint by vendorId, retrieves
     * the respective list of Items and calculates
     * the profit.
     * @param id id of the vendor in Long format
     * @return the profit as Double
     */
    public Double getItemsByVendorId(Long id) {
        log.info("Inside getUserWithDepartment of UserService");
        Double profit;
        String gatewayIp = "http://" + (System.getenv("GATEWAYIP") == null ? "localhost" : System.getenv("GATEWAYIP")) + ":8080";

        ResponseEntity<List<Item>> responseEntity =
                restTemplate.exchange(gatewayIp+"/history/generate/vendor/" +id,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Item>>() {
                        });
        List<Item> listOfItems = responseEntity.getBody();

        profit = calculateProfit(listOfItems);

        return profit;
    }

    /**
     * Calculates the profit by a list of items.
     * @param transactionList List of Items
     * @return the profit as Double
     */
    public Double calculateProfit(List<Item> transactionList){
        Double profit = 0.0;

        for (Item it : transactionList){
            profit += it.getPrice();
        }

        return profit;
    }
}
