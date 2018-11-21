/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.logs.controller;

import com.mi.hundsun.oxchains.provider.logs.cache.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 枫亭
 * @description TODO
 * @date 2018-04-05 21:39.
 */
@RestController
public class LogsController {

    @Autowired
    DiscoveryClient discoveryClient;

    @Value("${server.port}")
    private String port;

    @Value("${spring.cloud.stream.bindings.output.destination}")
    private String ssss;

    @Value("${server.allow.origin}")
    private String origin;

    @Value("${allow.origin}")
    private String origins;

    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("/dc")
    public String dc(){
        String services = "Services: " + discoveryClient.getServices();
        System.out.println(services);
        return services;
    }

    @GetMapping("/fromTo")
    public String fromTo(@RequestParam String name){

        return origins + "," + origin + ","+ssss + ",hi " + name + ",i am from " + port;
    }

    @RequestMapping("/redisTestSet")
    public String testRedisSet(@RequestParam String name){
        redisUtil.set(name,name);
        return name;
    }

    @RequestMapping("/redisTestGet")
    public String testRedisGet(@RequestParam String key){
        Object o = redisUtil.get(key);
        if(null == o) {
            return "have no this key";
        }
        return o.toString();
    }

}
