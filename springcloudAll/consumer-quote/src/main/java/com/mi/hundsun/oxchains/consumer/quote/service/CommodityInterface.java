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
public interface CommodityInterface {

    /**
     *
     */
    @RequestMapping(value = "/quote/commodity/getSymbolsByBaseCodeAndCode", method = RequestMethod.POST)
    ResultEntity getSymbolsByBaseCodeAndCode(@RequestParam("partition") String partition,
                        @RequestParam("code") String code,
                        @RequestParam("orderColumn") String orderColumn,
                        @RequestParam("orderType") String orderType,
                        @RequestParam("pageNumber") int pageNumber,
                        @RequestParam("pageSize") int pageSize);

    @RequestMapping(value = "/quote/commodity/getByCode", method = RequestMethod.POST)
    ResultEntity getByCode(@RequestParam("partition") String partition,
                              @RequestParam("code") String code);

    @RequestMapping(value = "/quote/commodity/selectByExchange", method = RequestMethod.POST)
    ResultEntity selectByExchange(@RequestParam("exchange") String exchange);

    @RequestMapping(value = "/quote/commodity/getByExchange", method = RequestMethod.POST)
    ResultEntity getByExchange(@RequestParam("exchange") String exchange);

    @RequestMapping(value = "/quote/commodity/searchForApp", method = RequestMethod.POST)
    ResultEntity searchForApp(@RequestParam("code") String code);

    @RequestMapping(value = "/quote/commodity/getDisplayOnAppCodes", method = RequestMethod.POST)
    ResultEntity getDisplayOnAppCodes();

    @RequestMapping(value = "/quote/commodity/getAllValidCodes", method = RequestMethod.POST)
    ResultEntity getAllValidCodes();

}
