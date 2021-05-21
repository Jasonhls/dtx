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
public class Bank2Controller {
    @Autowired
    AccountInfoService accountInfoService;

    //接受张三转账
    @GetMapping(value = "/transfer")
    public Boolean transfer(Double amount) {
        accountInfoService.updateAccountBalance("2", amount);
        return true;
    }
}
