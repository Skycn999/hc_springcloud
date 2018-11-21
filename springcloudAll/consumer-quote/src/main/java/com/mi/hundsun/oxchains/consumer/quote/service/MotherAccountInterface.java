/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.quote.service;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.po.quote.Commodity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 枫亭
 * @description TODO
 * @date 2018-04-12 18:01.
 */
@FeignClient("provider-quote-${feignSuffix}")
public interface MotherAccountInterface {

    @RequestMapping(value = "/quote/exchange/getAccountInfoByExchange", method = RequestMethod.POST)
    ResultEntity getAccountInfoByExchange(@RequestParam("exchangeNo") String exchangeNo);

}
