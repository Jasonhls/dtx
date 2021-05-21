package com.cn.feign;

import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 10:47
 **/
@FeignClient(value = "tcc-demo-bank2", fallback = Bank2ClientFallback.class)
public interface Bank2Client {
    @GetMapping(value = "/bank2/transfer")
    @Hmily//把全局事务信息带给调用的微服务
    Boolean transfer(@RequestParam("amount") Double amount);
}
