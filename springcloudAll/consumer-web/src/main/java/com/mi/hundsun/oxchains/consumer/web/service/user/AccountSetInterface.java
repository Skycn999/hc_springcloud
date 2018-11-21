package com.mi.hundsun.oxchains.consumer.web.service.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author db
 * @date 2018-04-14 18:07.
 */
@FeignClient("provider-user-${feignSuffix}")
public interface AccountSetInterface {

    @PostMapping(value = "/prod/account/set/myAccountSetInfo")
    ResultEntity myAccountSetInfo(@RequestParam("userId") Integer loginUserId);


    @PostMapping(value = "/prod/account/set/bindMobile")
    ResultEntity bindMobile(@RequestParam("userId") Integer loginUserId,@RequestParam("mobile") String mobile);

    @PostMapping(value = "/prod/account/set/bindEmail")
    ResultEntity bindEmail(@RequestParam("userId") Integer loginUserId,@RequestParam("email") String email);

    @PostMapping(value = "/prod/account/set/doModifyLoginPwd")
    ResultEntity doModifyLoginPwd(@RequestParam("username") String username,
                                  @RequestParam("authKey") String authKey,
                                  @RequestParam("newPwd") String newPwd,
                                  @RequestParam("newConfirmPwd") String newConfirmPwd);


    @PostMapping(value = "/prod/account/set/getUserAddressList")
    ResultEntity getUserAddressList(@RequestParam("userId") Integer userId,@RequestParam("pageSize") Integer pageSize,@RequestParam("pageNumber") Integer pageNumber);


    @PostMapping(value = "/prod/account/set/addUserAddress")
    ResultEntity addUserAddress(@RequestParam("userId") Integer userId,
                                @RequestParam("code") String code,
                                @RequestParam("address") String address,
                                @RequestParam("remark") String remark,
                                @RequestParam("isDefault") Integer isDefault);



    @PostMapping(value = "/prod/account/set/delUserAddress")
    ResultEntity delUserAddress(@RequestParam("userId") Integer userId,@RequestParam("id") Integer id);

    @PostMapping(value = "/prod/account/set/delUserAddressForApp")
    ResultEntity delUserAddressForApp(@RequestParam("userId") Integer userId,@RequestParam("idStr") String idStr);

    @PostMapping(value = "/prod/account/set/setMentionPwd")
    ResultEntity setMentionPwd(@RequestParam("userId") Integer userId,@RequestParam("newPwd") String newPwd,@RequestParam("newConfirmPwd") String newConfirmPwd);


    @PostMapping(value = "/prod/account/set/doModifyMentionPwd")
    ResultEntity doModifyMentionPwd(@RequestParam("userId") Integer userId,@RequestParam("newPwd") String newPwd,@RequestParam("newConfirmPwd") String newConfirmPwd);

    @PostMapping(value = "/prod/account/set/doResetBackMentionPwd")
    ResultEntity doResetBackMentionPwd(@RequestParam("userId") Integer userId,@RequestParam("username") String username,
                                       @RequestParam("authKey") String authKey,@RequestParam("newPwd") String newPwd,
                                       @RequestParam("newConfirmPwd") String newConfirmPwd);


    @PostMapping("/prod/account/set/generateGoogleAuthKey")
    ResultEntity generateGoogleAuthKey(@RequestParam("uuid") String uuid);


    @PostMapping("/prod/account/set/bindGoogleAuthKeyForApp")
    ResultEntity bindGoogleAuthKeyForApp(@RequestParam("uuid") String uuid,
                                         @RequestParam("type") Integer type,
                                         @RequestParam("authKey") String authKey,
                                         @RequestParam("googleCode") String googleCode);

    @PostMapping("/prod/account/set/bindGoogleAuthKey")
    ResultEntity bindGoogleAuthKey(@RequestParam("uuid") String uuid,
                                    @RequestParam("type") Integer type,
                                    @RequestParam("code") String code,
                                    @RequestParam("googleCode") String googleCode);

    @PostMapping("/prod/account/set/checkOldGoogleAuthKey")
    ResultEntity checkOldGoogleAuthKey(@RequestParam("uuid") String uuid,
                                   @RequestParam("type") Integer type,
                                   @RequestParam("code") String code,
                                   @RequestParam("googleCode") String googleCode);

    @PostMapping("/prod/account/set/modifyGoogleAuthKey")
    ResultEntity modifyGoogleAuthKey(@RequestParam("uuid") String uuid,
                                       @RequestParam("authKey") String authKey,
                                       @RequestParam("googleCode") String googleCode);

    @PostMapping("/prod/account/set/checkCloseOrOpenGoogleAuth")
    ResultEntity checkCloseOrOpenGoogleAuth(@RequestParam("uuid") String uuid,@RequestParam("state") Integer state);


    @PostMapping("/prod/account/set/checkCodeForApp")
    ResultEntity checkCodeForApp(@RequestParam("uuid") String uuid,@RequestParam("type") Integer type,@RequestParam("code") String code);


    @PostMapping("/prod/account/set/checkCodeCloseOrOpenForApp")
    ResultEntity checkCodeCloseOrOpenForApp(@RequestParam("uuid") String uuid,@RequestParam("type") Integer type,@RequestParam("code") String code);

    @PostMapping("/prod/account/set/doCloseOrOpenGoogleAuth")
    ResultEntity doCloseOrOpenGoogleAuth(@RequestParam("uuid") String uuid,
                                     @RequestParam("type") Integer type,
                                     @RequestParam("googleState") Integer googleState,
                                     @RequestParam("code") String code,
                                     @RequestParam("googleCode") String googleCode);

    @PostMapping("/prod/account/set/doCloseOrOpenGoogleAuthForApp")
    ResultEntity doCloseOrOpenGoogleAuthForApp(@RequestParam("uuid") String uuid,
                                         @RequestParam("type") Integer type,
                                         @RequestParam("googleState") Integer googleState,
                                         @RequestParam("authKey") String authKey,
                                         @RequestParam("googleCode") String googleCode);


    @PostMapping(value = "/prod/account/set/getUserAddressListForApp")
    ResultEntity getUserAddressListForApp(@RequestParam("userId") Integer userId,@RequestParam("code") String code,
                                          @RequestParam("pageSize") Integer pageSize,@RequestParam("pageNumber") Integer pageNumber);

    @PostMapping(value = "/prod/account/set/getUserAddressSizeForApp")
    ResultEntity getUserAddressSizeForApp(@RequestParam("userId") Integer userId);
}
