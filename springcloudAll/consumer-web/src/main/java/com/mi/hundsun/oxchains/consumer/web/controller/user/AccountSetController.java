/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.controller.user;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.config.CaptchaHelper;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.authResult.AuthResult;
import com.mi.hundsun.oxchains.base.common.utils.*;
import com.mi.hundsun.oxchains.base.core.common.HttpUtils;
import com.mi.hundsun.oxchains.base.core.constant.CacheID;
import com.mi.hundsun.oxchains.base.core.constant.CoinCode;
import com.mi.hundsun.oxchains.base.core.constant.ConfigNID;
import com.mi.hundsun.oxchains.base.core.constant.Constants;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.user.UserAddress;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.consumer.web.config.WebGenericController;
import com.mi.hundsun.oxchains.consumer.web.service.user.AccountCentralInterface;
import com.mi.hundsun.oxchains.consumer.web.service.user.AccountSetInterface;
import com.mi.hundsun.oxchains.consumer.web.service.user.RegistLoginInterface;
import com.mi.hundsun.oxchains.consumer.web.service.user.UserInterface;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 枫亭
 * @description 账户设置
 * @date 2018-04-13 19:46.
 */
@RestController
@RequestMapping("/api/web/account/set")
public class AccountSetController extends WebGenericController {

    @Autowired
    AccountCentralInterface accountCentralInterface;
    @Autowired
    AccountSetInterface accountSetInterface;
    @Autowired
    UserInterface userInterface;
    @Autowired
    RegistLoginInterface registLoginInterface;

    @ApiOperation(value = "账户设置信息", notes = "获取账户设置信息-如" +
            "账户等级(1-低,2-中,3-高,4-非常高); 各认证类型的状态。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户id", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
    })
    @RequestMapping("/myAccountSetInfo")
    public ResultEntity myAccountSetInfo() {
        //1.查询各认证类型的状态(手机认证、邮箱认证、登录密码、提币密码、谷歌验证器、实名认证)。 2.根据认证状态计算账户安全等级
        ResultEntity resultEntity = accountSetInterface.myAccountSetInfo(getLoginUserId());
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        }
        return fail();
    }

    @ApiOperation(value = "绑定手机号码", notes = "绑定手机号码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @RequestMapping("/bindMobile")
    public ResultEntity bindMobile(@RequestParam String mobile, @RequestParam String code) {
        //0.校验参数和校验手机号码格式
        if (!ValidateUtils.isPhone(mobile)) {
            throw new BussinessException("手机号码格式错误");
        }
        //1.校验用户输入的验证码是否一致。
        ResultEntity resultEntity = registLoginInterface.checkCodeForBackPwd(mobile, code, Constants.BIND_MOBILE);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return fail("验证码错误");
        }
        //2.保存用户信息.并更新用户认证表信息
        ResultEntity result = accountSetInterface.bindMobile(getLoginUserId(), mobile);
        if (result.getCode() != ResultEntity.SUCCESS) {
            return fail("绑定失败");
        }
        return ok();
    }

    @ApiOperation(value = "绑定邮箱", notes = "绑定邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @RequestMapping("/bindEmail")
    public ResultEntity bindEmail(@RequestParam String email, @RequestParam String code) {
        //0.校验参数和校验邮箱格式
        if (!ValidateUtils.isEmail(email)) {
            throw new BussinessException("邮箱格式错误");
        }
        //2.校验用户输入的验证码是否一致。
        ResultEntity resultEntity = registLoginInterface.checkCodeForBackPwd(email, code, Constants.BIND_EMAIL);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return fail("验证码错误");
        }
        //3.保存用户信息,并更新用户认证表信息
        ResultEntity result = accountSetInterface.bindEmail(getLoginUserId(), email);
        if (result.getCode() != ResultEntity.SUCCESS) {
            return fail("绑定失败");
        }
        return ok();
    }


    @ApiOperation(value = "获取(绑定手机/邮箱)短信或邮箱验证码", notes = "通过用户填入的手机号码或邮箱获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "username", value = "手机号码/邮箱", required = true, dataType = "String")
    })
    @RequestMapping("/getBindCode")
    public ResultEntity getBindCode(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            throw new BussinessException("参数错误");
        }
        //校验格式
        if (!ValidateUtils.isEmail(username) && !ValidateUtils.isPhone(username)) {
            throw new BussinessException("参数格式错误");
        }
        //校验是否存在
        Users users = registLoginInterface.findByUsername(username);
        if (null != users) {
            if (username.contains("@")) {
                return fail("绑定的邮箱已存在");
            } else {
                return fail("绑定的手机号码已存在");
            }
        }
        //发送验证码
        if (username.contains("@")) {
            registLoginInterface.sendEmailOrSms(username, Constants.BIND_EMAIL);
        } else {
            registLoginInterface.sendEmailOrSms(username, Constants.BIND_MOBILE);
        }
       return ok();
    }


    @ApiOperation(value = "获取(修改登录密码)短信或邮箱验证码", notes = "通过用户填入的手机号码或邮箱获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "username", value = "手机号码/邮箱", required = true, dataType = "String")
    })
    @RequestMapping("/getModifyPwdCode")
    public ResultEntity getModifyPwdCode(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            throw new BussinessException("参数错误");
        }
        //校验格式
        if (!ValidateUtils.isEmail(username) && !ValidateUtils.isPhone(username)) {
            throw new BussinessException("参数格式错误");
        }
        //校验是否已认证
        ResultEntity resultEntity;
        if (username.contains("@")) {
            resultEntity = userInterface.checkEmailAuth(getLoginUserId(), username);
            if (resultEntity.getCode() != ResultEntity.SUCCESS) {
                return resultEntity;
            }
        } else {
            resultEntity = userInterface.checkMobileAuth(getLoginUserId(), username);
            if (resultEntity.getCode() != ResultEntity.SUCCESS) {
                return resultEntity;
            }
        }
        //发送验证码
        if (username.contains("@")) {
            registLoginInterface.sendEmailOrSms(username, Constants.MODIFY_PWD_EMAIL);
        } else {
            registLoginInterface.sendEmailOrSms(username, Constants.MODIFY_PWD_MOBILE);
        }
        return ok();
    }


    @ApiOperation(value = "修改登录密码校验", notes = "修改登录密码-下一步按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "username", value = "手机号码/邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "type", value = "1,手机号码/2,邮箱", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @RequestMapping("/nextModifyLoginPwd")
    public ResultEntity nextModifyLoginPwd(@RequestParam String username, @RequestParam Integer type, @RequestParam String code) {
        //0.校验参数和校验手机号码格式以及校验用户输入的验证码是否一致。
        if (type == Constants.MOBILE_TYPE) {
            if (!ValidateUtils.isPhone(username)) {
                throw new BussinessException("手机号码格式错误");
            }
            ResultEntity resultEntity = registLoginInterface.checkCodeForBackPwd(username, code, Constants.MODIFY_PWD_MOBILE);
            if (resultEntity.getCode() != ResultEntity.SUCCESS) {
                return fail("验证码错误");
            }
        } else if (type == Constants.EMAIL_TYPE) {
            if (!ValidateUtils.isEmail(username)) {
                throw new BussinessException("邮箱格式错误");
            }
            ResultEntity resultEntity = registLoginInterface.checkCodeForBackPwd(username, code, Constants.MODIFY_PWD_EMAIL);
            if (resultEntity.getCode() != ResultEntity.SUCCESS) {
                return fail("验证码错误");
            }
        } else {
            return fail("验证类型不正确");
        }
        //生成一个authKey回传到前台，用于鉴定重设密码接口是否允许执行
        String s = RandomUtils.randomCustomUUID();
        registLoginInterface.saveCode(CacheID.MODIFY_LOGIN_PWD_KEY_PREFIX + username, s);
        return ok(s);
    }

    @ApiOperation(value = "执行修改登录密码操作", notes = "执行修改登录密码操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "username", value = "手机号/邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "authKey", value = "authKey", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptNewPwd", value = "加密后的密码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptConfirmNewPwd", value = "加密后的确认密码", required = true, dataType = "String")
    })
    @RequestMapping("/doModifyLoginPwd")
    public ResultEntity doModifyLoginPwd(@RequestParam String username,
                                         @RequestParam String authKey,
                                         @RequestParam String encryptNewPwd,
                                         @RequestParam String encryptConfirmNewPwd) throws Exception {
        //0.校验参数
        if (StrUtil.isBlank(username) || StrUtil.isBlank(authKey) || StrUtil.isBlank(encryptNewPwd) || StrUtil.isBlank(encryptConfirmNewPwd)) {
            return fail("参数错误");
        }
        //1.判断两次输入的密码是否一致
        if (!RSAUtils.decrypt(encryptNewPwd).equals(RSAUtils.decrypt(encryptConfirmNewPwd))) {
            return fail("两次输入的密码不一致");
        }
        //校验authKey
        //2.生成新的密码盐并设置新的密码到用户信息并更新用户密码
        accountSetInterface.doModifyLoginPwd(username, authKey, encryptNewPwd, encryptConfirmNewPwd);
        return ok();
    }

    @ApiOperation(value = "设置提币密码", notes = "设置提币密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptNewPwd", value = "加密后的密码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptConfirmNewPwd", value = "加密后的确认密码", required = true, dataType = "String")
    })
    @RequestMapping("/setMentionPwd")
    public ResultEntity setMentionPwd(@RequestParam String uuid, @RequestParam String encryptNewPwd,
                                      @RequestParam String encryptConfirmNewPwd) throws Exception {
        //0.校验参数
        if (StringUtils.isBlank(encryptNewPwd) || StringUtils.isBlank(encryptConfirmNewPwd)) {
            return fail("参数不能为空");
        }
        //1.判断两次输入的密码是否一致
        if (!RSAUtils.decrypt(encryptNewPwd).equals(RSAUtils.decrypt(encryptConfirmNewPwd))) {
            return fail("两次密码不一致");
        }
        Users users = userInterface.selectByUuid(uuid);
        if (!StringUtils.isBlank(users.getMentionPwd())) {
            return fail("已设置提币密码");
        }
        //2.生成新的密码盐并设置新的密码到用户信息
        //3.更新用户密码
        ResultEntity resultEntity = accountSetInterface.setMentionPwd(getLoginUserId(), encryptNewPwd, encryptConfirmNewPwd);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok();
        }
        return fail("设置提币密码失败");
    }


    @ApiOperation(value = "修改提币密码操作", notes = "修改提币密码操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "mentionPwd", value = "加密后的原提币密码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptNewPwd", value = "加密后的新密码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptConfirmNewPwd", value = "加密后的确认密码", required = true, dataType = "String")
    })
    @RequestMapping("/doModifyMentionPwd")
    public ResultEntity doModifyMentionPwd(@RequestParam String uuid, @RequestParam String mentionPwd,
                                           @RequestParam String encryptNewPwd,
                                           @RequestParam String encryptConfirmNewPwd) throws Exception {
        //0.校验参数
        if (StringUtils.isBlank(mentionPwd) || StringUtils.isBlank(encryptNewPwd) || StringUtils.isBlank(encryptConfirmNewPwd)) {
            return fail("参数不能为空");
        }
        //1.判断两次输入的密码
        if (!RSAUtils.decrypt(encryptNewPwd).equals(RSAUtils.decrypt(encryptConfirmNewPwd))) {
            return fail("两次密码不一致");
        }
        if (RSAUtils.decrypt(mentionPwd).equals(RSAUtils.decrypt(encryptNewPwd))) {
            return fail("新密码不能与原密码一致");
        }
        //校验提币密码
        Users users = userInterface.selectByUuid(uuid);
        String pwd = RSAUtils.decrypt(mentionPwd);
        if (!MD5Utils.getMd5(pwd, users.getMentionPwdSalt()).equals(users.getMentionPwd())) {
            return fail("提币密码不正确");
        }
        //2.生成新的密码盐并设置新的密码到用户信息
        //3.更新用户密码
        ResultEntity resultEntity = accountSetInterface.doModifyMentionPwd(getLoginUserId(), encryptNewPwd, encryptConfirmNewPwd);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok();
        }
        return fail("修改提币密码失败");
    }

    @ApiOperation(value = "获取图文验证码", notes = "获取图文验证码-找回提币密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "codeKey", value = "图文验证码key", required = true, dataType = "String")
    })
    @RequestMapping("/getImageCode")
    public ResultEntity getImageCode(@RequestParam String codeKey) throws Exception {
        String captcha = new CaptchaHelper().createCaptcha();
        registLoginInterface.saveCode(CacheID.GRAPHIC_CODE_PREFIX + codeKey, captcha);
        return ok();
    }

    @ApiOperation(value = "获取短信验证码(找回提币密码)-下一步", notes = "获取短信验证码(找回提币密码)-下一步")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "graphicCodeKey", value = "图文验证码key", required = true, dataType = "String")
            , @ApiImplicitParam(name = "graphicCode", value = "用户输入的图文验证码", required = true, dataType = "String")
    })
    @RequestMapping("/getBackMentionPwdCodeByMobile")
    public ResultEntity getBackMentionPwdCodeByMobile(@RequestParam String mobile, @RequestParam String graphicCodeKey, @RequestParam String graphicCode) {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(graphicCodeKey) || StringUtils.isBlank(graphicCode)) {
            throw new BussinessException("参数错误");
        }
        //校验格式
        if (!ValidateUtils.isPhone(mobile)) {
            throw new BussinessException("参数格式错误");
        }
        //校验是否手机认证
        ResultEntity resultEntity = userInterface.checkMobileAuth(getLoginUserId(), mobile);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return resultEntity;
        }
        //图文验证码校验
        ResultEntity result1 = registLoginInterface.checkGraphicCode(graphicCodeKey, graphicCode);
        if (result1.getCode() != ResultEntity.SUCCESS) {
            return result1;
        }
        //发送验证码
        registLoginInterface.sendEmailOrSms(mobile, Constants.BACK_MENTION_PWD_MOBILE);
        return ok();
    }


    @ApiOperation(value = "获取邮箱验证码(找回提币密码)-下一步", notes = "获取邮箱验证码(找回提币密码)-下一步")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "graphicCodeKey", value = "图文验证码key", required = true, dataType = "String")
            , @ApiImplicitParam(name = "graphicCode", value = "用户输入的图文验证码", required = true, dataType = "String")
    })
    @RequestMapping("/getBackMentionPwdCodeByEmail")
    public ResultEntity getBackMentionPwdCodeByEmail(@RequestParam String email, @RequestParam String graphicCodeKey, @RequestParam String graphicCode) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(graphicCodeKey) || StringUtils.isBlank(graphicCode)) {
            throw new BussinessException("参数错误");
        }
        //校验格式
        if (!ValidateUtils.isEmail(email)) {
            throw new BussinessException("参数格式错误");
        }
        //校验是否邮箱认证
        ResultEntity resultEntity = userInterface.checkEmailAuth(getLoginUserId(), email);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return resultEntity;
        }
        //图文验证码校验
        ResultEntity result1 = registLoginInterface.checkGraphicCode(graphicCodeKey, graphicCode);
        if (result1.getCode() != ResultEntity.SUCCESS) {
            return result1;
        }
        registLoginInterface.sendEmailOrSms(email, Constants.BACK_MENTION_PWD_EMAIL);
        return ok();
    }


    @ApiOperation(value = "获取验证码(找回提币密码)-APP", notes = "获取验证码(找回提币密码)-APP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "username", value = "手机号码/邮箱", required = true, dataType = "String")
    })
    @RequestMapping("/getBackMentionPwdCodeForApp")
    public ResultEntity getBackMentionPwdCodeForApp(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            throw new BussinessException("参数错误");
        }
        //校验格式
        if (!ValidateUtils.isEmail(username) && !ValidateUtils.isPhone(username)) {
            throw new BussinessException("参数格式错误");
        }
        if (username.contains("@")) {
            //校验是否邮箱认证
            ResultEntity resultEntity = userInterface.checkEmailAuth(getLoginUserId(), username);
            if (resultEntity.getCode() != ResultEntity.SUCCESS) {
                return resultEntity;
            }
            registLoginInterface.sendEmailOrSms(username, Constants.BACK_MENTION_PWD_EMAIL);
            return ok();
        }else {
            //校验是否手机认证
            ResultEntity resultEntity = userInterface.checkMobileAuth(getLoginUserId(), username);
            if (resultEntity.getCode() != ResultEntity.SUCCESS) {
                return resultEntity;
            }
            registLoginInterface.sendEmailOrSms(username, Constants.BACK_MENTION_PWD_MOBILE);
             return ok();
        }
    }


    @ApiOperation(value = "找回提币密码下一步-手机", notes = "找回提币密码下一步-手机")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @RequestMapping("/nextBackMentionPwdByMobile")
    public ResultEntity nextModifyMentionPwd(@RequestParam String mobile, @RequestParam String code) {
        //0.校验参数和校验手机号码格式
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(code)) {
            throw new BussinessException("参数错误");
        }
        //校验手机号码格式是否正确
        if (!ValidateUtils.isPhone(mobile)) {
            throw new BussinessException("手机号码格式错误");
        }
        //1.校验验证码是否正确
        ResultEntity resultEntity = registLoginInterface.checkCodeForBackPwd(mobile, code, Constants.BACK_MENTION_PWD_MOBILE);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return fail("验证码错误");
        }
        //3,生成一个authKey回传到前台，用于鉴定重设密码接口是否允许执行
        String s = RandomUtils.randomCustomUUID();
        registLoginInterface.saveCode(CacheID.BACK_PWD_KEY_PREFIX + mobile, s);
        return ok(s);
    }


    @ApiOperation(value = "找回提币密码下一步-邮箱", notes = "找回提币密码下一步-邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @RequestMapping("/nextBackMentionPwdByEmail")
    public ResultEntity nextBackMentionPwdByEmail(@RequestParam String email, @RequestParam String code) {
        //0.校验参数和校验手机号码格式
        if (StringUtils.isBlank(email) || StringUtils.isBlank(code)) {
            throw new BussinessException("参数错误");
        }
        //校验手机号码格式是否正确
        if (!ValidateUtils.isEmail(email)) {
            throw new BussinessException("邮箱格式错误");
        }
        //1.校验验证码是否正确
        ResultEntity resultEntity = registLoginInterface.checkCodeForBackPwd(email, code, Constants.BACK_MENTION_PWD_EMAIL);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return fail("验证码错误");
        }
        //3,生成一个authKey回传到前台，用于鉴定重设密码接口是否允许执行
        String s = RandomUtils.randomCustomUUID();
        registLoginInterface.saveCode(CacheID.BACK_PWD_KEY_PREFIX + email, s);
        return ok(s);
    }


    @ApiOperation(value = "找回提币密码", notes = "找回提币密码-手机号码/邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "username", value = "手机号码/邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "authKey", value = "authKey", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptNewPwd", value = "加密的新密码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptConfirmNewPwd", value = "加密的二次新密码", required = true, dataType = "String")
    })
    @RequestMapping("/doResetBackMentionPwd")
    public ResultEntity doResetBackMentionPwd(@RequestParam String username,
                                              @RequestParam String authKey,
                                              @RequestParam String encryptNewPwd,
                                              @RequestParam String encryptConfirmNewPwd) throws Exception {
        //1.校验参数
        if (StrUtil.isBlank(username) || StrUtil.isBlank(authKey) || StrUtil.isBlank(encryptNewPwd) || StrUtil.isBlank(encryptConfirmNewPwd)) {
            return fail("参数错误");
        }
        if (!RSAUtils.decrypt(encryptNewPwd).equals(RSAUtils.decrypt(encryptConfirmNewPwd))) {
            return fail("两次输入的密码不一致");
        }
        ResultEntity resultEntity = accountSetInterface.doResetBackMentionPwd(getLoginUserId(), username, authKey, encryptNewPwd, encryptConfirmNewPwd);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return resultEntity;
        }
        return ok();
    }


    @ApiOperation(value = "用户绑定谷歌验证器接口", notes = "用户绑定谷歌验证器接口-返回key和二维码地址和手机号/邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
    })
    @RequestMapping("/getGoogleAuthKeyForBind")
    public ResultEntity getGoogleAuthKeyForBind(@RequestParam String uuid) {
        Users users = userInterface.selectByUuid(uuid);
        if (!StringUtils.isBlank(users.getGoogleKey())) {
            return fail("已绑定谷歌验证器");
        }
        ResultEntity resultEntity = accountSetInterface.generateGoogleAuthKey(uuid);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        } else {
            return fail("获取谷歌验证信息失败");
        }
    }

    @ApiOperation(value = "获取验证码-绑定谷歌验证器的时候使用", notes = "获取验证码-绑定谷歌验证器的时候使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "type", value = "发送验证码类型 1，手机号码，2，邮箱", required = true, dataType = "Integer")
    })
    @RequestMapping("/getCodeForGoogleAuthKey")
    public ResultEntity getCodeForGoogleAuthKey(@RequestParam String uuid, @RequestParam Integer type) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(uuid)) {
            return fail("参数错误");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("参数类型错误");
        }
        Users users = userInterface.selectByUuid(uuid);
        if (type == Constants.MOBILE_TYPE) {
            registLoginInterface.sendEmailOrSms(users.getMobile(), Constants.BIND_GOOGLE_KEY_MOBILE);
        } else {
            registLoginInterface.sendEmailOrSms(users.getEmail(), Constants.BIND_GOOGLE_KEY_EMAIl);
        }
        return ok();
    }


    @ApiOperation(value = "校验验证码-绑定谷歌验证器-APP", notes = "校验验证码-绑定谷歌验证器-APP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "Stjiring")
            , @ApiImplicitParam(name = "type", value = "发送验证码类型 1，手机号码，2，邮箱", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @RequestMapping("/checkCodeForApp")
    public ResultEntity checkCodeForApp(@RequestParam String uuid, @RequestParam Integer type,@RequestParam String code) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(type)||StringUtils.isBlank(code)) {
            return fail("参数错误");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("类型参数错误");
        }
        ResultEntity resultEntity = accountSetInterface.checkCodeForApp(uuid, type, code);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return resultEntity;
        }
        return resultEntity;
    }


    @ApiOperation(value = "绑定Google验证码-APP", notes = "绑定Google验证码-APP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "Stjiring")
            , @ApiImplicitParam(name = "type", value = "发送验证码类型 1，手机号码，2，邮箱", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "authKey", value = "authKey", required = true, dataType = "String")
            , @ApiImplicitParam(name = "googleCode", value = "谷歌验证码", required = true, dataType = "String")
    })
    @RequestMapping("/bindGoogleAuthKeyForApp")
    public ResultEntity bindGoogleAuthKeyForApp(@RequestParam String uuid, @RequestParam Integer type, @RequestParam String authKey, @RequestParam String googleCode) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(type) || StringUtils.isBlank(googleCode) || StringUtils.isBlank(authKey)) {
            return fail("参数错误");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("类型参数错误");
        }
        return accountSetInterface.bindGoogleAuthKeyForApp(uuid, type, authKey,googleCode);
    }


    @ApiOperation(value = "绑定Google验证码", notes = "绑定Google验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "type", value = "类型 1手机号码，2，邮箱", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "googleCode", value = "谷歌验证码", required = true, dataType = "String")
    })
    @RequestMapping("/bindGoogleAuthKey")
    public ResultEntity bindGoogleAuthKey(@RequestParam String uuid, @RequestParam Integer type, @RequestParam String code, @RequestParam String googleCode) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(type) || StringUtils.isBlank(code) || StringUtils.isBlank(googleCode)) {
            return fail("参数错误");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("类型参数错误");
        }
        ResultEntity resultEntity = accountSetInterface.bindGoogleAuthKey(uuid, type, code, googleCode);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok();
        }
        return resultEntity;
    }


    @ApiOperation(value = "用户修改谷歌验证器接口", notes = "用户修改谷歌验证器接口-返回key和二维码地址和手机号/邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
    })
    @RequestMapping("/getGoogleAuthKeyForModify")
    public ResultEntity getGoogleAuthKeyForModify(@RequestParam String uuid) {
        Users users = userInterface.selectByUuid(uuid);
        if (StringUtils.isBlank(users.getGoogleKey())) {
            return fail("未绑定谷歌验证器");
        }
        ResultEntity resultEntity = accountSetInterface.generateGoogleAuthKey(uuid);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        } else {
            return fail("获取谷歌验证信息失败");
        }
    }


    @ApiOperation(value = "获取验证码-修改谷歌验证器的时候使用", notes = "获取验证码-修改谷歌验证器的时候使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "type", value = "类型 1手机号码，2，邮箱", required = true, dataType = "Integer")
    })
    @RequestMapping("/getCodeForModifyGoogleKey")
    public ResultEntity getCodeForModifyGoogleKey(@RequestParam String uuid, @RequestParam Integer type) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(uuid)) {
            return fail("参数错误");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("参数类型错误");
        }
        Users users = userInterface.selectByUuid(uuid);
        if (type == Constants.MOBILE_TYPE) {
            registLoginInterface.sendEmailOrSms(users.getMobile(), Constants.MODIFY_GOOGLE_KEY_MOBILE);
        } else {
            registLoginInterface.sendEmailOrSms(users.getEmail(), Constants.MODIFY_GOOGLE_KEY_EMAIL);
        }
        return ok();
    }

    @ApiOperation(value = "校验原谷歌验证码-修改Google验证码", notes = "校验原谷歌验证码-修改Google验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "type", value = "类型 1手机号码，2，邮箱", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "googleCode", value = "原谷歌验证码", required = true, dataType = "String")
    })
    @RequestMapping("/checkOldGoogleAuthKey")
    public ResultEntity checkOldGoogleAuthKey(@RequestParam String uuid, @RequestParam Integer type, @RequestParam String code, @RequestParam String googleCode) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(type) || StringUtils.isBlank(code) || StringUtils.isBlank(googleCode)) {
            return fail("参数错误");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("类型参数错误");
        }
        return accountSetInterface.checkOldGoogleAuthKey(uuid, type, code, googleCode);
    }


    @ApiOperation(value = "修改Google验证码", notes = "修改Google验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "authKey", value = "authKey", required = true, dataType = "String")
            , @ApiImplicitParam(name = "googleCode", value = "原谷歌验证码", required = true, dataType = "String")
    })
    @RequestMapping("/modifyGoogleAuthKey")
    public ResultEntity modifyGoogleAuthKey(@RequestParam String uuid,  @RequestParam String authKey, @RequestParam String googleCode) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(authKey) || StringUtils.isBlank(googleCode)) {
            return fail("参数错误");
        }
        ResultEntity resultEntity = accountSetInterface.modifyGoogleAuthKey(uuid, authKey,  googleCode);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok();
        }
        return resultEntity;
    }



    @ApiOperation(value = "开启/关闭谷歌验证器校验", notes = "开启/关闭谷歌验证器校验-返回手机号/邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "state", value = "进行开启/关闭操作 0，关闭， 1，开启", required = true, dataType = "Integer")
    })
    @RequestMapping("/checkCloseOrOpenGoogleAuth")
    public ResultEntity checkCloseOrOpenGoogleAuth(@RequestParam String uuid, @RequestParam Integer state) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(state)) {
            return fail("参数错误");
        }
        return accountSetInterface.checkCloseOrOpenGoogleAuth(uuid, state);
    }


    @ApiOperation(value = "获取验证码-开启/关闭谷歌验证器", notes = "获取验证码-开启/关闭谷歌验证器")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "type", value = "类型 1手机号码，2，邮箱", required = true, dataType = "Integer")
    })
    @RequestMapping("/getCodeForCloseOrOpenGoogle")
    public ResultEntity getCodeForCloseOrOpenGoogle(@RequestParam String uuid, @RequestParam Integer type) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(uuid)) {
            return fail("参数错误");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("参数类型错误");
        }
        Users users = userInterface.selectByUuid(uuid);
        if (type == Constants.MOBILE_TYPE) {
            registLoginInterface.sendEmailOrSms(users.getMobile(), Constants.CLOSE_OR_OPEN_GOOGLE_AUTH_MOBILE);
        } else {
            registLoginInterface.sendEmailOrSms(users.getEmail(), Constants.CLOSE_OR_OPEN_GOOGLE_AUTH_EMAIL);
        }
        return ok();
    }


    @ApiOperation(value = "校验验证码-关闭/开启Google验证器-APP", notes = "校验验证码-关闭/开启Google验证器-APP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "Stjiring")
            , @ApiImplicitParam(name = "type", value = "发送验证码类型 1，手机号码，2，邮箱", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @RequestMapping("/checkCodeCloseOrOpenForApp")
    public ResultEntity checkCodeCloseOrOpenForApp(@RequestParam String uuid, @RequestParam Integer type, @RequestParam String code) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(type) || StringUtils.isBlank(code)) {
            return fail("参数错误");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("类型参数错误");
        }
        return accountSetInterface.checkCodeCloseOrOpenForApp(uuid, type, code);
    }

    @ApiOperation(value = "关闭/开启Google验证器-APP", notes = "关闭/开启Google验证器-APP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "Stjiring")
            , @ApiImplicitParam(name = "type", value = "发送验证码类型 1，手机号码，2，邮箱", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "googleState", value = "进行开启/关闭操作 0，关闭， 1，开启", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "authKey", value = "authKey", required = true, dataType = "String")
            , @ApiImplicitParam(name = "googleCode", value = "谷歌验证码", required = true, dataType = "String")
    })
    @RequestMapping("/doCloseOrOpenGoogleAuthForApp")
    public ResultEntity doCloseOrOpenGoogleAuthForApp(@RequestParam String uuid, @RequestParam Integer type, @RequestParam Integer googleState, @RequestParam String authKey, @RequestParam String googleCode) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(type) || StringUtils.isBlank(googleState) || StringUtils.isBlank(authKey) || StringUtils.isBlank(googleCode)) {
            return fail("参数错误");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("类型参数错误");
        }
        return accountSetInterface.doCloseOrOpenGoogleAuthForApp(uuid, type, googleState, authKey, googleCode);
    }


    @ApiOperation(value = "关闭/开启Google验证器", notes = "关闭/开启Google验证器")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "type", value = "类型 1手机号码，2，邮箱", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "googleState", value = "进行开启/关闭操作 0，关闭， 1，开启", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "googleCode", value = "谷歌验证码", required = true, dataType = "String")
    })
    @RequestMapping("/doCloseOrOpenGoogleAuth")
    public ResultEntity doCloseOrOpenGoogleAuth(@RequestParam String uuid, @RequestParam Integer type, @RequestParam Integer googleState, @RequestParam String code, @RequestParam String googleCode) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(type) || StringUtils.isBlank(googleState) || StringUtils.isBlank(code) || StringUtils.isBlank(googleCode)) {
            return fail("参数错误");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("类型参数错误");
        }
        return accountSetInterface.doCloseOrOpenGoogleAuth(uuid, type, googleState, code, googleCode);
    }


    @ApiOperation(value = "我的提币地址列表", notes = "我的提币地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
    })
    @RequestMapping("/getUserAddressList")
    public ResultEntity getUserAddressList(@RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        ResultEntity entity = accountSetInterface.getUserAddressList(getLoginUserId(), pageSize, pageNumber);
        if (entity.getCode() == ResultEntity.SUCCESS) {
            return ok(entity.getData());
        }
        return fail();
    }


    @ApiOperation(value = "我的提币地址数量统计-APP", notes = "我的提币地址数量统计-APP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
    })
    @RequestMapping("/getUserAddressSizeForApp")
    public ResultEntity getUserAddressSizeForApp() {
        return accountSetInterface.getUserAddressSizeForApp(getLoginUserId());
    }


    @ApiOperation(value = "我的提币地址列表-APP", notes = "我的提币地址列表-APP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "code", value = "币种", required = true, dataType = "String")
    })
    @RequestMapping("/getUserAddressListForApp")
    public ResultEntity getUserAddressListForApp(@RequestParam Integer pageSize, @RequestParam Integer pageNumber, @RequestParam String code) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(pageSize) || StringUtils.isBlank(pageNumber)) {
            return fail("参数错误");
        }
        if (!(code.equals(CoinCode.BTC) || code.equals(CoinCode.ETH))) {
            return fail("币种错误");
        }
        ResultEntity entity = accountSetInterface.getUserAddressListForApp(getLoginUserId(), code, pageSize, pageNumber);
        if (entity.getCode() == ResultEntity.SUCCESS) {
            return ok(entity.getData());
        }
        return fail();
    }


    @ApiOperation(value = "添加提币地址", notes = "添加提币地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "code", value = "币种", required = true, dataType = "String")
            , @ApiImplicitParam(name = "address", value = "地址", required = true, dataType = "String")
            , @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "String")
            , @ApiImplicitParam(name = "isDefault", value = "是否默认0,否 1,是", required = true, dataType = "Integer")
    })
    @RequestMapping("/addUserAddress")
    public ResultEntity addUserAddress(@RequestParam String code, @RequestParam String address, @RequestParam String remark, @RequestParam Integer isDefault) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(address) || StringUtils.isBlank(remark) || StringUtils.isBlank(isDefault)) {
            return fail("参数错误");
        }
        if (isDefault != UserAddress.ISDEFAULT.NO.code && isDefault != UserAddress.ISDEFAULT.YES.code) {
            return fail("是否默认参数错误");
        }
        ResultEntity entity = accountSetInterface.addUserAddress(getLoginUserId(), code, address, remark, isDefault);
        if (entity.getCode() == ResultEntity.SUCCESS) {
            return ok(entity.getData());
        }
        return fail();
    }


    @ApiOperation(value = "删除提币地址-APP", notes = "删除提币地址-APP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "idStr", value = "提币地址idStr", required = true, dataType = "String")
    })
    @RequestMapping("/delUserAddressForApp")
    public ResultEntity delUserAddressForApp(@RequestParam String idStr) {
        if (StringUtils.isBlank(idStr)) {
            return fail("参数错误");
        }
        ResultEntity entity = accountSetInterface.delUserAddressForApp(getLoginUserId(), idStr);
        if (entity.getCode() != ResultEntity.SUCCESS) {
            return fail("删除失败");
        }
        return ok();
    }


    @ApiOperation(value = "删除提币地址", notes = "删除提币地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "id", value = "提币地址id", required = true, dataType = "Integer")
    })
    @RequestMapping("/delUserAddress")
    public ResultEntity delUserAddress(@RequestParam Integer id) {
        if (StringUtils.isBlank(id)) {
            return fail("参数错误");
        }
        ResultEntity entity = accountSetInterface.delUserAddress(getLoginUserId(), id);
        if (entity.getCode() != ResultEntity.SUCCESS) {
            return fail("删除失败");
        }
        return ok();
    }


    @ApiOperation(value = "实名认证-国内用户", notes = "实名认证国内用户信息提交")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "realname", value = "用户姓名", required = true, dataType = "String")
            , @ApiImplicitParam(name = "idCardNo", value = "身份证号码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "idCardFrontPicUuid", value = "身份证正面图片uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "idCardReversePicUuid", value = "身份证反面图片uuid", required = true, dataType = "String")
    })
    @RequestMapping("/idCardIdentify")
    public ResultEntity idCardIdentify(@RequestParam String realname,
                                       @RequestParam String idCardNo,
                                       @RequestParam String idCardFrontPicUuid,
                                       @RequestParam String idCardReversePicUuid) {
        if (StrUtil.isBlank(realname)) {
            throw new BussinessException("[真实姓名]不能为空");
        }
        if (StrUtil.isBlank(idCardNo)) {
            throw new BussinessException("[身份证号码]不能为空");
        }
        if (StrUtil.isBlank(idCardFrontPicUuid)) {
            throw new BussinessException("[身份证正面图片]不能为空");
        }
        if (StrUtil.isBlank(idCardReversePicUuid)) {
            throw new BussinessException("[身份证反面图片]不能为空");
        }
        if (!ValidateUtils.isContainChinese(realname)) {
            return fail("姓名格式不正确");
        }
        if (!ValidateUtils.isCard(idCardNo)) {
            return fail("身份证号格式不正确");
        }
        //调用第三方接口验证真实性
        String host = "https://eidimage.shumaidata.com";
        String path = "/eid_image/get";
        String method = "POST";
        RedisService redisService = SpringContextUtils.getBean(RedisService.class);
        String appcode = redisService.get(ConfigNID.APPCODE);
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("idcard", idCardNo);
        bodys.put("name", realname);
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            System.out.println(response.toString());
            //获取response的body
//            System.out.println(EntityUtils.toString(response.getEntity()));
            String json = EntityUtils.toString(response.getEntity());
            AuthResult authResult =  JSON.parseObject(json, AuthResult.class);
            if (authResult.getCode().equals("0")){
                //通过后保存用户信息
                ResultEntity resultEntity = userInterface.idCardIdentify(getLoginUserId(), realname, idCardNo, idCardFrontPicUuid, idCardReversePicUuid);
                if (resultEntity.getCode() != ResultEntity.SUCCESS) {
                    return fail("认证失败");
                }
            }else {
                return fail(authResult.getMessage());
            }
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail("认证失败");
        }
    }

    @ApiOperation(value = "实名认证-海外用户", notes = "实名认证海外用户信息提交")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "realname", value = "用户姓名", required = true, dataType = "String")
            , @ApiImplicitParam(name = "passportNo", value = "护照号码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "passportPicUuid", value = "护照正面图片Uuid", required = true, dataType = "String")
    })
    @RequestMapping("/passportIdentify")
    public ResultEntity passportIdentify(@RequestParam String realname,
                                         @RequestParam String passportNo,
                                         @RequestParam String passportPicUuid) {
        if (StrUtil.isBlank(realname)) {
            throw new BussinessException("[真实姓名]不能为空");
        }
        if (StrUtil.isBlank(passportNo)) {
            throw new BussinessException("[护照号码]不能为空");
        }
        if (StrUtil.isBlank(passportPicUuid)) {
            throw new BussinessException("[护照图片]不能为空");
        }

        return userInterface.passportIdentify(getLoginUserId(), realname, passportNo, passportPicUuid);
    }

}
