/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 枫亭
 * @date 2018-04-05 21:07.
 */
@SpringBootApplication(exclude = RabbitAutoConfiguration.class)
@EnableDiscoveryClient
@MapperScan(basePackages = "com.mi.hundsun.oxchains.base.core.mapper")
@ComponentScan(basePackages = {
        "com.mi.hundsun.oxchains.base.common.mybatis",
        "com.mi.hundsun.oxchains.base.common.utils",
        "com.mi.hundsun.oxchains.provider.admin",
        "com.mi.hundsun.oxchains.base.core"})
public class ProviderAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderAdminApplication.class,args);
    }
}
