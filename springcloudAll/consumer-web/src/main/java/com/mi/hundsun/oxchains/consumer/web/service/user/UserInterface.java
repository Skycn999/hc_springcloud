/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.service.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.po.tpl.Protocol;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 枫亭
 * @date 2018-04-14 18:07.
 */
@FeignClient("provider-user-${feignSuffix}")
public interface UserInterface {

    @PostMapping("/prod/user/selectByUuid")
    Users selectByUuid(@RequestParam("uuid") String uuid);

    @PostMapping("/prod/user/selectByEmail")
    Users selectByEmail(@RequestParam("username") String username);

    @PostMapping("/prod/user/selectByMobile")
    Users selectByMobile(@RequestParam("username") String username);


    @PostMapping("/prod/user/getMyMsg")
    ResultEntity getMyMsg(@RequestParam("userId") Integer userId, @RequestParam("pageSize") Integer pageSize,
                          @RequestParam("pageNumber") Integer pageNumber);

    @PostMapping("/prod/user/clearMyMsg")
    ResultEntity clearMyMsg(@RequestParam("userId") Integer userId, @RequestParam("ids") List<Integer> ids);

    @PostMapping("/prod/user/idCardIdentify")
    ResultEntity idCardIdentify(@RequestParam("userId") Integer userId,
                                @RequestParam("realname") String realname,
                                @RequestParam("idCardNo") String idCardNo,
                                @RequestParam("idCardFrontPic") String idCardFrontPic,
                                @RequestParam("idCardReversePic") String idCardReversePic);

    @PostMapping("/prod/user/passportIdentify")
    ResultEntity passportIdentify(@RequestParam("userId") Integer userId,
                                  @RequestParam("realname") String realname,
                                  @RequestParam("passportNo") String passportNo,
                                  @RequestParam("passportPic") String passportPic);

    @PostMapping("/prod/user/preValidUserInfoToTx")
    ResultEntity preValidUserInfoToTx(@RequestParam("userId") Integer userId, @RequestParam("netWorth") BigDecimal netWorth);

    @PostMapping("/prod/user/getTradeFeeTpl")
    ResultEntity getTradeFeeTpl(@RequestParam("currencyPair") String currencyPair);

    @PostMapping("/prod/user/findServiceFeeTplByUserId")
    ResultEntity findServiceFeeTplByUserId(@RequestParam("userId") Integer userId, @RequestParam("code") String code);

    @PostMapping("/prod/user/findExchangeMotherAccountList")
    ResultEntity findExchangeMotherAccountList(@RequestParam("exchange") String exchange);

    @PostMapping("/prod/user/checkMentionCoinState")
    boolean checkMentionCoinState(@RequestParam("userId") Integer userId);

    @PostMapping("/prod/user/checkMobileAuth")
    ResultEntity checkMobileAuth(@RequestParam("userId") Integer userId, @RequestParam("mobile") String mobile);

    @PostMapping("/prod/user/checkEmailAuth")
    ResultEntity checkEmailAuth(@RequestParam("userId") Integer userId, @RequestParam("email") String email);

    @PostMapping("/prod/user/getRegistProtocol")
    ResultEntity getRegistProtocol();

    @PostMapping("/prod/user/getRiskProtocol")
    ResultEntity getRiskProtocol();

    @PostMapping("/prod/user/distributeAddressToUser")
    void distributeAddressToUser(@RequestParam("userId") Integer userId);
}
