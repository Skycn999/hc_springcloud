/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 枫亭
 * @description 管理控制台对外服务
 * @date 2018-04-05 21:07.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients
@ComponentScan(basePackages = {
        "com.mi.hundsun.oxchains.base.common.config",
        "com.mi.hundsun.oxchains.consumer.admin",
        "com.mi.hundsun.oxchains.base.common.utils",
        "com.mi.hundsun.oxchains.consumer.admin.config",
})
public class ConsumerAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerAdminApplication.class,args);
    }

}
