/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.service.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 枫亭
 * @date 2018-04-14 18:07.
 */
@FeignClient("provider-user-${feignSuffix}")
public interface AccountCentralInterface {

    @PostMapping(value = "/prod/account/central/getUserIdentifyInfo")
    ResultEntity getUserIdentifyInfo(@RequestParam("userId") Integer loginUserId);

    @PostMapping(value = "/prod/account/central/checkMyMentionCoin")
    ResultEntity checkMyMentionCoin(@RequestParam("userId") Integer loginUserId);

    @PostMapping(value = "/prod/fn/mentionCoin/checkAddress")
    boolean checkAddress(@RequestParam("userId") Integer loginUserId,
                         @RequestParam("address") String address);

    @PostMapping(value = "/prod/user/checkMentionPwd")
    boolean checkMentionPwd(@RequestParam("userId") Integer userId,
                            @RequestParam("encryptMentionPwd") String encryptMentionPwd);

    @PostMapping(value = "/prod/user/riskControl/checkUserServiceFeeTpl")
    boolean checkUserServiceFeeTpl(@RequestParam("code") String code,
                                   @RequestParam("serviceFee") String serviceFee);

    @PostMapping(value = "/prod/fn/mentionCoin/myMentionCoinLog")
    ResultEntity myMentionCoinLog(@RequestParam("userId") Integer userId,@RequestParam("pageSize") Integer pageSize,
                                     @RequestParam("pageNumber") Integer pageNumber);

    @PostMapping(value = "/prod/account/central/checkMentionPwdForSafe")
    ResultEntity checkMentionPwdForSafe(@RequestParam("userId") Integer loginUserIdd, @RequestParam("mentionPwd") String mentionPwd);


    @PostMapping(value = "/prod/fn/mentionCoin/addMentionCoin")
    ResultEntity addMentionCoin(@RequestParam("userId") Integer userId,  @RequestParam("address") String address,
                                @RequestParam("coin") String coin,@RequestParam("amount") String amount,@RequestParam("serviceFee") String serviceFee );

    @PostMapping(value = "/prod/account/central/checkMaxAmount")
    ResultEntity checkMaxAmount(@RequestParam("userId") Integer userId, @RequestParam("code") String code,@RequestParam("amount") String amount );

    @PostMapping(value = "/prod/fn/mentionCoin/findMyMcAddressByCode")
    ResultEntity findMyMcAddressByCode(@RequestParam("userId") Integer userId,  @RequestParam("code") String code);
}
