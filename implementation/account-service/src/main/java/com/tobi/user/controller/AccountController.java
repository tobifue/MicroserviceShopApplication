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

    @GetMapping("/{id}")
    public Double getProfitByVendorId(@PathVariable("id") Long userId){

        log.info("Inside getUserWithDepartment of UserController");
        return accountService.getItemsByVendorId(userId);
    }
}


