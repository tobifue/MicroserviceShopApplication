package com.tobi.user.controller;

import com.tobi.user.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;
/*
    @PostMapping("/")
    public User saveUser(@RequestBody User user){
        log.info("Inside saveUser of UserController");
        return userService.saveUser(user);
    }
*/
    @GetMapping("/{id}")
    public Integer getProfitByVendorId(@PathVariable("id") Long userId){

        log.info("Inside getUserWithDepartment of UserController");
        return accountService.getItemsByVendorId(userId);
    }
}
