package com.cn.feign;

import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 10:48
 **/
@Component
public class Bank2ClientFallback implements Bank2Client{
    @Override
    public String transfer(Double amount) {
        return "fallback";
    }
}
