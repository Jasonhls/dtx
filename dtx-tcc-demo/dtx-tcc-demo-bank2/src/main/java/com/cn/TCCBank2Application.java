package com.cn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 13:35
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableFeignClients(basePackages = {"com.cn.feign"})
@ComponentScan({"com.cn", "org.dromara.hmily"})
public class TCCBank2Application {
    public static void main(String[] args) {
        SpringApplication.run(TCCBank2Application.class, args);
    }
}
