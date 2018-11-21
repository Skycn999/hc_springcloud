/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 枫亭
 * @date 2018-05-02 13:54.
 */
@FeignClient("${bianTradeFeignName}")
public interface TradeBianInterface {

    /**
     * 火币-下单接口
     * @param api_key         交易所对应apikey
     * @param api_secret      交易所对应apisecret
     * @param symbol          交易对
     * @param entrust_bs      买卖方向1买入 2卖出
     * @param entrust_prop    委托类型  F1市价委托  F2 限价委托
     * @param entrust_price   下单价格  市价可不填或填0
     * @param entrust_amount  下单数量
     * @return          {
    "code":200,
    "msg":"success",
    "data":{
    "entrust_no":"2"  委托编号
    }
    }
     */
    @PostMapping(value = "/digiccy/v1/trade/order")
    String order(@RequestParam("api_key") String api_key
            , @RequestParam("api_secret") String api_secret
            , @RequestParam("symbol") String symbol
            , @RequestParam("entrust_bs") String entrust_bs
            , @RequestParam("entrust_prop") String entrust_prop
            , @RequestParam("entrust_price") String entrust_price
            , @RequestParam("entrust_amount") String entrust_amount
    );

    /**
     * 订单查询
     * @param api_key     apikey
     * @param api_secret  apisecret
     * @param entrust_no  委托编号
     * @param symbol      交易对
     * @return
     * {
            "code":200,
            "msg":"success",
            "data":[
                {
                    "symbol":"1",
                    "entrust_no":"1",
                    "entrust_price":"1",
                    "entrust_amount":"1001",
                    "entrust_bs":"1",
                    "entrust_status":"8",
                    "entrust_prop":"F1",
                    "business_detail":[
                        {
                        "business_amount":"1001",
                        "business_no":"1",
                        "business_price":"1"
                        }
                    ]
                }
            ]
        }
     */
    @GetMapping(value = "/digiccy/v1/trade/order_qry")
    String orderQry(@RequestParam("api_key") String api_key
            , @RequestParam("api_secret") String api_secret
            , @RequestParam("symbol") String symbol
            , @RequestParam("entrust_no") String entrust_no
    );

    /**
     * 撤单
     * @param api_key         交易所对应apikey
     * @param api_secret      交易所对应apisecret
     * @param symbol          交易对
     * @param entrust_no      委托编号
     * @return 撤单id
     */
    @PostMapping(value = "/digiccy/v1/trade/withdraw")
    String withdraw(@RequestParam("api_key") String api_key
            , @RequestParam("api_secret") String api_secret
            , @RequestParam("symbol") String symbol
            , @RequestParam("entrust_no") String entrust_no
    );

    /**
     * 持仓查询
     * @param api_key         交易所对应apikey
     * @param api_secret      交易所对应apisecret
     * @param account_id      币币交易账户id
     * @return
     * {
            "code":200,
            "msg":"success",
            "data":[
                {
                    "currentcy_code":"btc",
                    "enable_balance":"1.0001",
                    "frozen_balance":"1.0000"
                },
                {
                    "currentcy_code":"eth",
                    "enable_balance":"100.0001",
                    "frozen_balance":"20.0000"
                }
            ]
        }
     */
    @GetMapping(value = "/digiccy/v1/trade/account")
    String account(@RequestParam("api_key") String api_key
            , @RequestParam("api_secret") String api_secret
            , @RequestParam("account_id") String account_id
    );
}
