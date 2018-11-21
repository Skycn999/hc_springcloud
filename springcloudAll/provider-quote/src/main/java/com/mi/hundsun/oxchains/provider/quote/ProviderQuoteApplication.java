/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.quote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 枫亭
 * @description TODO
 * @date 2018-04-05 21:07.
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = "com.mi.hundsun.oxchains.base.core.mapper")
@ComponentScan(basePackages = {
        "com.mi.hundsun.oxchains.base.common.interceptor",
        "com.mi.hundsun.oxchains.base.common.mybatis",
        "com.mi.hundsun.oxchains.base.common.utils",
        "com.mi.hundsun.oxchains.provider.quote",
        "com.mi.hundsun.oxchains.base.core"})
public class ProviderQuoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderQuoteApplication.class,args);
    }
}
