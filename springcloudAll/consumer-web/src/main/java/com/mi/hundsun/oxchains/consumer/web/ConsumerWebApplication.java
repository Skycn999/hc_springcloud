/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 枫亭
 * @date 2018-04-05 21:07.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableHystrixDashboard
@EnableDiscoveryClient
@ComponentScan(basePackages = {
        "com.mi.hundsun.oxchains.base.common.config",
        "com.mi.hundsun.oxchains.consumer.web",
        "com.mi.hundsun.oxchains.base.common.utils",
        "com.mi.hundsun.oxchains.base.core.service",
        "com.mi.hundsun.oxchains.base.core.interceptors.web"
})
public class ConsumerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerWebApplication.class,args);
    }

}
