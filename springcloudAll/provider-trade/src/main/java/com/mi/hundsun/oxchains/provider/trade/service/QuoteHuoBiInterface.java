/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 枫亭
 * @desc 行情接口
 * @date 2018-04-11 15:41.
 */
@FeignClient("${huobiQuoteFeignName}")
public interface QuoteHuoBiInterface {

    /**
     * 获取分时数据
     *
     * @param symbol 交易对
     * @param size   请求数量
     * @return json
     * [
        {
        "last_px":"7339.5100000000",
        "timestamp":"1522732500999",
        "business_amount":"6.7901163682"
        },
        {
        "last_px":"7326.6900000000",
        "timestamp":"1522732560999",
        "business_amount":"10.8393849656"
        }]
     */
    @RequestMapping(value = "/digiccy/v1/market/trend", method = RequestMethod.GET)
    String trend(@RequestParam("symbol") String symbol, @RequestParam("size") String size);

    /**
     * 获取交易对最新价 可以传入多个交易对，以逗号分隔
     * @param symbols 交易对串
     * @return json
     */
    @RequestMapping(value = "/digiccy/v1/market/price", method = RequestMethod.GET)
    String price(@RequestParam("symbols") String symbols);

    /**
     * 获取K线数据
     * @param symbol     交易对
     * @param kline_type K线类型
     * @param size       请求数量
     * @return json
     */
    @RequestMapping(value = "/digiccy/v1/market/kline", method = RequestMethod.GET)
    String kline(@RequestParam("symbol") String symbol, @RequestParam("kline_type") String kline_type, @RequestParam("size") String size);

    /**
     * 获取分笔数据
     * @param symbol 交易对
     * @param size   请求数量
     * @return json
     */
    @RequestMapping(value = "/digiccy/v1/market/tick", method = RequestMethod.GET)
    String tick(@RequestParam("symbol") String symbol, @RequestParam("size") String size);

    /**
     * 获取聚合10档行情
     * @param symbol 交易对
     * @return json
     */
    @RequestMapping(value = "/digiccy/v1/market/depth", method = RequestMethod.GET)
    String depth(@RequestParam("symbol") String symbol);

    /**
     * 获取聚合10档行情
     * @param symbol 交易对
     * @return json
     */
    @RequestMapping(value = "/digiccy/v1/market/depth", method = RequestMethod.GET)
    String depth(@RequestParam("symbol") String symbol, @RequestParam("exchangeType") String exchangeType);


}
