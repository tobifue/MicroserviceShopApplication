package com.tobi.user.service;

import com.tobi.user.VO.Item;
import com.tobi.user.repository.AccountRepository;
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
    private AccountRepository accountRepository;

    @Autowired
    private RestTemplate restTemplate;
/*
    public User saveUser(User user) {
        log.info("Inside saveUser of UserService");
        return accountRepository.save(user);
    }
*/
    public Integer getItemsByVendorId(Long id) {
        log.info("Inside getUserWithDepartment of UserService");

        Integer profit = 0;
        ResponseEntity<List<Item>> responseEntity =
                restTemplate.exchange("http://INVENTORY-SERVICE/inventory/vendor/" +id,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Item>>() {
                        });
        List<Item> listOfItems = responseEntity.getBody();

        for (Item it : listOfItems){
            profit += it.getPrice();
        }


        return profit;
    }
}
