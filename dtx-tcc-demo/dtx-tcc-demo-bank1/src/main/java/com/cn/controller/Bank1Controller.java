package com.cn.controller;

import com.cn.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 10:51
 **/
@RestController
public class Bank1Controller {
    @Autowired
    AccountInfoService accountInfoService;

    //张三转账
    @GetMapping(value = "/transfer")
    public String transfer(Double amount) {
        accountInfoService.updateAccountBalance("1", amount);
        return "bank1 " + amount;
    }
}
