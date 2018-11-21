/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.quartz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @author 枫亭
 * @description TODO
 * @date 2018-04-05 21:07.
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
public class QuartzClusterApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzClusterApplication.class, args);
        System.out.println("****简单Quartz-Cluster微服务****已启动.");
    }
}
