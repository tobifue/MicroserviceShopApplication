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

@Service
@Slf4j
public class AccountService {

    @Autowired
    private RestTemplate restTemplate;

    public Double getItemsByVendorId(Long id) {
        log.info("Inside getUserWithDepartment of UserService");
        Double profit = 0.0;
        Long vendorId = 0L;
        ResponseEntity<List<Item>> responseEntity =
                restTemplate.exchange("http://localhost:8080/history/generate/seller/" +id,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Item>>() {
                        });
        List<Item> listOfItems = responseEntity.getBody();

        for (Item it : listOfItems){
            profit += it.getPrice();
        }

        return profit;
    }
}
