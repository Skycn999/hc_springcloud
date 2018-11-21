/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.logs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 枫亭
 * @description TODO
 * @date 2018-04-05 21:07.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ProviderLogsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderLogsApplication.class,args);
    }
}
