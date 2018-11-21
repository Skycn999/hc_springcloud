/*
 * Copyright (c) 2015-2018, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.service.tx;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author 枫亭
 * @date 2018-04-14 18:07.
 */
@FeignClient("provider-trade-${feignSuffix}")
public interface AccountInterface {

    @PostMapping(value = "/prod/tx/registerInit")
    ResultEntity registerInit(@RequestParam("userId") Integer userId);

    @PostMapping(value = "/tx/account/selectByUserId")
    ResultEntity selectByUserId(@RequestParam("userId") Integer userId);

    @PostMapping(value = "/tx/account/getNetWorth")
    BigDecimal getNetWorth(@RequestParam("userId") Integer userId);

    @PostMapping(value = "/tx/account/checkAvailAmount")
    boolean checkAvailAmount(@RequestParam("userId") Integer userId,
                             @RequestParam("code") String code,
                             @RequestParam("amount") String amount,
                             @RequestParam("serviceFee") String serviceFee
    );

    @PostMapping(value = "/tx/account/doMentionCoin")
    ResultEntity doMentionCoin(@RequestParam("userId") Integer userId,
                               @RequestParam("code") String code,
                               @RequestParam("amount") String amount,
                               @RequestParam("serviceFee") String serviceFee
    );

    @PostMapping(value = "/tx/account/getAvailMentionCurrency")
    ResultEntity getAvailMentionCurrency(@RequestParam("userId") Integer loginUserId,
                                         @RequestParam("isMerge") Integer isMerge
    );

    @PostMapping(value = "/tx/account/findAvailAccount")
    ResultEntity findAvailAccount(@RequestParam("userId") Integer userId,
                                  @RequestParam("symbol") String symbol);

    @PostMapping(value = "/tx/account/selectOne")
    Account selectOne(@RequestBody Account account);

    @PostMapping(value = "/tx/account/handleAccountByMarketBuyIn")
    ResultEntity handleAccountByMarketBuyIn(@RequestParam("userId") Integer userId,
                                            @RequestParam("gmv") BigDecimal gmv,
                                            @RequestParam("buyFeeScale") BigDecimal buyFeeScale,
                                            @RequestParam("currencyPair") String currencyPair,
                                            @RequestParam("exchangeNo") String exchangeNo
    );

    @PostMapping(value = "/tx/account/handleAccountByMarketSellOut")
    ResultEntity handleAccountByMarketSellOut(@RequestParam("userId") Integer userId,
                                              @RequestParam("gmv") BigDecimal gmv,
                                              @RequestParam("sellFeeScale") BigDecimal sellFeeScale,
                                              @RequestParam("currencyPair") String currencyPair,
                                              @RequestParam("exchangeNo") String exchangeNo,
                                              @RequestParam("accountNo") String accountNo
    );

    @PostMapping(value = "/tx/account/handleAccountByLimitedBuyIn")
    ResultEntity handleAccountByLimitedBuyIn(@RequestParam("userId") Integer userId,
                                             @RequestParam("price") BigDecimal price,
                                             @RequestParam("amount") BigDecimal amount,
                                             @RequestParam("buyFeeScale") BigDecimal buyFeeScale,
                                             @RequestParam("currencyPair") String currencyPair,
                                             @RequestParam("exchangeNo") String exchangeNo
    );

    @PostMapping(value = "/tx/account/handleAccountByLimitedSellOut")
    ResultEntity handleAccountByLimitedSellOut(@RequestParam("userId") Integer userId,
                                               @RequestParam("price") BigDecimal price,
                                               @RequestParam("amount") BigDecimal amount,
                                               @RequestParam("sellFeeScale") BigDecimal sellFeeScale,
                                               @RequestParam("currencyPair") String currencyPair,
                                               @RequestParam("exchangeNo") String exchangeNo);
}
