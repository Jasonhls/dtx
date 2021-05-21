package com.cn.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 10:47
 **/
@FeignClient(value = "seata-demo-bank2", fallback = Bank2ClientFallback.class)
public interface Bank2Client {
    @GetMapping(value = "/bank2/transfer")
    String transfer(@RequestParam("amount") Double amount);
}
