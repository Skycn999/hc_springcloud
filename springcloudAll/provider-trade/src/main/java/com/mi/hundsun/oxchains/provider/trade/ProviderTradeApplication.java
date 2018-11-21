/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 枫亭
 * @date 2018-04-05 21:07.
 */
@EnableDiscoveryClient
@SpringBootApplication()
@MapperScan(basePackages = "com.mi.hundsun.oxchains.base.core.tx.mapper")
@ComponentScan(basePackages = {
      "com.mi.hundsun.oxchains.base.common.interceptor",
        "com.mi.hundsun.oxchains.base.common.mybatis",
        "com.mi.hundsun.oxchains.base.common.utils",
        "com.mi.hundsun.oxchains.provider.trade",
        "com.mi.hundsun.oxchains.base.core"
})
@EnableFeignClients
public class ProviderTradeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderTradeApplication.class, args);
    }
}
