/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.service.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.po.quote.CodePairConfig;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author 枫亭
 * @date 2018-04-14 18:07.
 */
@FeignClient("provider-user-${feignSuffix}")
public interface ExchangeInterface {

    @PostMapping(value = "/prod/exchange/findMotherAccounts")
    ResultEntity findMotherAccounts(@RequestParam("exchangeNo") String exchangeNo,
                                    @RequestParam("code") String code);

    @PostMapping(value = "/prod/exchange/findMotherAccountByExNoAndAccountName")
    ResultEntity findMotherAccountByExNoAndAccountName(@RequestParam("exchangeNo") String exchangeNo,
                                                         @RequestParam("accountName") String accountName);

    @PostMapping(value = "/prod/exchange/getCodePairConfig")
    ResultEntity getCodePairConfig(@RequestParam("currencyPair") String currencyPair);

    @PostMapping(value = "/prod/exchange/getSymbolsConfigs")
    ResultEntity getSymbolsConfigs(@RequestParam("symbols") String symbols);
}
