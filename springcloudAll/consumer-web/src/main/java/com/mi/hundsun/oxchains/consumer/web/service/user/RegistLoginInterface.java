/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.service.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 枫亭
 * @date 2018-04-13 21:35.
 */
@FeignClient("provider-user-${feignSuffix}")
public interface RegistLoginInterface {

    @PostMapping("/prod/registLogin/validLoginInfo")
    ResultEntity validLoginInfo(@RequestParam("username") String username,
                                @RequestParam("encryptPwd") String encryptPwd);

    @PostMapping("/prod/registLogin/getGraphicCode")
    String getGraphicCode(@RequestParam("graphicCodeKey") String graphicCodeKey);

    @PostMapping("/prod/registLogin/saveCode")
    ResultEntity saveCode(@RequestParam("key") String key,
                          @RequestParam("code") String code);

    @PostMapping("/prod/registLogin/checkGraphicCode")
    ResultEntity checkGraphicCode(@RequestParam("graphicCodeKey") String graphicCodeKey,
                                  @RequestParam("graphicCode") String graphicCode);

    @PostMapping("/prod/registLogin/checkCodeForRegist")
    ResultEntity checkCodeForRegist(@RequestParam("username") String username,
                                    @RequestParam("code") String verifyCode,
                                    @RequestParam("type") String type);


    @PostMapping("/prod/registLogin/getCodeForBackPwd")
    ResultEntity getCodeForBackPwd(@RequestParam("email") String email,
                                   @RequestParam("graphicCodeKey") String graphicCodeKey,
                                   @RequestParam("graphicCode") String graphicCode,
                                   @RequestParam("type") String type);

    @PostMapping("/prod/registLogin/checkCodeForBackPwd")
    ResultEntity checkCodeForBackPwd(@RequestParam("username") String username,
                                     @RequestParam("code") String code,
                                     @RequestParam("type") String type);

    @PostMapping("/prod/registLogin/getRsaPublicKey")
    ResultEntity getRsaPublicKey();

    @PostMapping("/prod/registLogin/register")
    ResultEntity register(@RequestParam("username") String username,
                          @RequestParam("encryptPwd") String encryptPwd);

    @PostMapping("/prod/registLogin/doResetBackPwd")
    ResultEntity doResetBackPwd(@RequestParam("username") String username,
                                @RequestParam("authKey") String authKey,
                                @RequestParam("newPwd") String newPwd,
                                @RequestParam("newConfirmPwd") String newConfirmPwd);

    @PostMapping("/prod/registLogin/findByUsername")
    Users findByUsername(@RequestParam("username") String username);

    @PostMapping("/prod/registLogin/updateLastLogin")
    ResultEntity updateLastLogin(@RequestParam("userId") Integer userId);


    @PostMapping("/prod/registLogin/checkSafeAuthForLogin")
    ResultEntity checkSafeAuthForLogin(@RequestParam("username") String username, @RequestParam("authKey") String authKey,
                                       @RequestParam("googleState") Integer googleState, @RequestParam("googleCode") String googleCode);

    @PostMapping("/prod/registLogin/sendEmailOrSms")
    void sendEmailOrSms(@RequestParam("username") String username, @RequestParam("type") String type);

    @PostMapping("/prod/registLogin/sendLetter")
    void sendLetter(@RequestParam("userId") Integer userId, @RequestParam("nid") String nid);
}
