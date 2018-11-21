/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.quote;

import com.xiaoleilu.hutool.cron.TaskExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author 枫亭
 * @date 2018-04-05 21:07.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan(basePackages = {
        "com.mi.hundsun.oxchains.consumer.quote"
})
public class ConsumerQuoteApplication {


//    @Primary
//    @Bean
//    public TaskExecutor primaryTaskExecutor() {
//        // Customize executor as appropriate
//        return new ThreadPoolTaskExecutor();
//    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerQuoteApplication.class, args);
    }

}
