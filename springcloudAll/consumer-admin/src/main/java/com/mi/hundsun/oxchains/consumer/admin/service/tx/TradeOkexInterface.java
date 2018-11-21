/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.admin.service.tx;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author 枫亭
 * @date 2018-05-02 13:54.
 */
@FeignClient("${okexTradeFeignName}")
public interface TradeOkexInterface {

    @RequestMapping(value = "/digiccy/v1/trade/account", method = RequestMethod.GET)
    String account(@RequestParam("api_key") String api_key, @RequestParam("api_secret") String api_secret) throws Exception;
}
