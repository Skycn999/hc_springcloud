/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.service;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 枫亭
 * @desc 交易所相关接口
 * @date 2018-04-11 15:41.
 */
@FeignClient("provider-user-${feignSuffix}")
public interface ExchangeInterface {

    @PostMapping(value = "/prod/exchange/findMotherAccount")
    MotherAccountInfoModel findMotherAccount(@RequestParam("exchangeNo") String exchangeNo, @RequestParam("code") String code);

    @PostMapping(value = "/prod/exchange/findMotherAccounts")
    ResultEntity findMotherAccounts(@RequestParam("exchangeNo") String exchangeNo,
                                    @RequestParam("code") String code);

    @PostMapping(value = "/prod/exchange/findMotherAccountByEac")
    ResultEntity findMotherAccountByEac(@RequestParam("exchangeNo") String exchangeNo,
                                        @RequestParam("accountNo") String accountNo);

    @PostMapping(value = "/prod/exchange/findMotherAccountByExNoAndAccountName")
    ResultEntity findMotherAccountByExNoAndAccountName(@RequestParam("exchangeNo") String exchangeNo,
                                                       @RequestParam("accountName") String accountName);
}
