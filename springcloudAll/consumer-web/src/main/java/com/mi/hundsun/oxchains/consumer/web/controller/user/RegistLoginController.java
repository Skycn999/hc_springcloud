/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.controller.user;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.config.CaptchaHelper;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.*;
import com.mi.hundsun.oxchains.base.core.constant.CacheID;
import com.mi.hundsun.oxchains.base.core.constant.Constants;
import com.mi.hundsun.oxchains.base.core.constant.MsgTempNID;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.consumer.web.config.WebGenericController;
import com.mi.hundsun.oxchains.consumer.web.config.WebInterceptor;
import com.mi.hundsun.oxchains.consumer.web.rabbitmq.UserCorrelationHandlerProducer;
import com.mi.hundsun.oxchains.consumer.web.service.tx.AccountInterface;
import com.mi.hundsun.oxchains.consumer.web.service.user.RegistLoginInterface;
import com.mi.hundsun.oxchains.consumer.web.service.user.UserInterface;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 枫亭
 * @description 登录注册
 * @date 2018-04-13 19:46.
 */
@RestController
@RequestMapping("/api/web/registLogin")
public class RegistLoginController extends WebGenericController {

    @Autowired
    RegistLoginInterface registLoginInterface;
    @Autowired
    UserInterface userInterface;
    @Autowired
    AccountInterface accountInterface;
    @Autowired
    UserCorrelationHandlerProducer userCorrelationHandlerProducer;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "手机号码/邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptPwd", value = "加密后的密码", required = true, dataType = "String")
    })
    @ApiOperation(value = "登录校验", notes = "登录校验")
    @RequestMapping("/doLogin")
    public ResultEntity doLogin(@RequestParam String username, @RequestParam String encryptPwd) {
        //参数校验
        if (StringUtils.isBlank(username) || StringUtils.isBlank(encryptPwd)) {
            throw new BussinessException("用户名密码不能为空");
        }
        Users user = registLoginInterface.findByUsername(username);
        if (null == user) {
            return result("", "您还没有注册,请先注册!", ResultEntity.NO_REGISTER);
        }
        ResultEntity entity = registLoginInterface.validLoginInfo(username, encryptPwd);
        if (entity.getCode() == ResultEntity.SUCCESS) {

            //校验是否绑定谷歌验证器并且开启
            boolean b = user.getGoogleState() == Users.GOOGLESTATE.open.code;

            //生成一个authKey回传到前台，用于鉴定重设密码接口是否允许执行
            String s = RandomUtils.randomCustomUUID();
            registLoginInterface.saveCode(CacheID.LOGIN_KEY_PREFIX + username, s);
            Map<String, Object> map = new HashMap<>();
            map.put("authKey", s);
            if (b && !StringUtils.isBlank(user.getGoogleKey())) {
                map.put("googleState", true);
            } else {
                map.put("googleState", false);
            }
            return ok(map);
        } else {
            return fail(entity.getMessage());
        }
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "手机号码/邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "authKey", value = "authKey", required = true, dataType = "String")
            , @ApiImplicitParam(name = "googleState", value = "是否需要验证谷歌验证器 0，不需要，1，需要", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "googleCode", value = "谷歌验证码", dataType = "String")
    })
    @ApiOperation(value = "安全验证并登录", notes = "安全验证并登录")
    @RequestMapping("/checkSafeAuthForLogin")
    public ResultEntity checkSafeAuthForLogin(@RequestParam String username, @RequestParam String authKey,
                                              @RequestParam Integer googleState, @RequestParam String googleCode) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(authKey) || StringUtils.isBlank(googleState)) {
            return fail("参数错误");
        }
        if (googleState != Users.GOOGLESTATE.open.code && googleState != Users.GOOGLESTATE.close.code) {
            return fail("参数错误");
        }
        if (googleState == Users.GOOGLESTATE.open.code && StringUtils.isBlank(googleCode)) {
            return fail("参数错误");
        }
        ResultEntity resultEntity = registLoginInterface.checkSafeAuthForLogin(username, authKey, googleState, googleCode);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            Users user = registLoginInterface.findByUsername(username);
            //更新最后登录时间
            ResultEntity result = registLoginInterface.updateLastLogin(user.getId());
            if (result.getCode() != ResultEntity.SUCCESS) {
                return fail("登录失败");
            }
            String sessionId = getRequest().getSession().getId();
            WebInterceptor.setLoginUser(user, sessionId);
            Map<String, Object> data = new HashMap<>();
            //firstLogin true第一次登录，false，则不是
            data.put("firstLogin", StringUtils.isBlank(user.getLastLoginDate()));
            //firstLogin true有google验证码，false，则没有
            data.put("googleKey", !StringUtils.isBlank(user.getGoogleKey()));
            data.put("sessionId", sessionId);
            data.put("uuid", user.getUuid());
            return ok(data);
        } else {
            return resultEntity;
        }
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
    })
    @ApiOperation(value = "退出登录", notes = "退出登录")
    @RequestMapping("/outLogin")
    public ResultEntity outLogin(@RequestParam String uuid, @RequestParam String sessionId) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(sessionId)) {
            return fail("参数错误");
        }
        //校验uuid
        Users user = userInterface.selectByUuid(uuid);
        if (null == user) {
            return fail("用户不存在");
        }
        //校验sessionId
        RedisService redisService = SpringContextUtils.getBean(RedisService.class);
        String userInfo = redisService.get(WebInterceptor.COOKIE_SESSION_ID + sessionId);
        if (StringUtils.isBlank(userInfo)) {
            return fail("用户信息有误或未登录");
        }
        String decrypt = ToolAES.decrypt(userInfo);
        if (StrUtil.isBlank(decrypt)) {
            return fail("用户信息有误");
        }
        if (!decrypt.contains(uuid)) {
            return fail("用户信息有误");
        }
        //清除登录缓存信息
        WebInterceptor.outLoginUser(uuid, sessionId);
        return ok();
    }

    @ApiOperation(value = "获取RSA加密公钥", notes = "获取RSA加密公钥")
    @RequestMapping("/getRsaPublicKey")
    public ResultEntity getRsaPublicKey() {
        return registLoginInterface.getRsaPublicKey();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "手机号码/邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptPwd", value = "加密后的密码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptConfirmPwd", value = "加密后的确认密码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "agreeProtocol", value = "同意注册协议", required = true, dataType = "String")
    })
    @ApiOperation(value = "注册", notes = "用户注册账号")
    @RequestMapping("/register")
    public ResultEntity register(@RequestParam String username, @RequestParam String verifyCode,
                                 @RequestParam String encryptPwd, @RequestParam String encryptConfirmPwd,
                                 @RequestParam String agreeProtocol) throws Exception {
        if (StrUtil.isBlank(username) || StrUtil.isBlank(verifyCode) || StrUtil.isBlank(encryptPwd) ||
                StrUtil.isBlank(encryptConfirmPwd)) {
            throw new BussinessException("参数错误");
        }
        if (!ValidateUtils.isEmail(username) && !ValidateUtils.isPhone(username)) {
            throw new BussinessException("参数格式错误");
        }
        if (!RSAUtils.decrypt(encryptPwd).equals(RSAUtils.decrypt(encryptConfirmPwd))) {
            throw new BussinessException("两次输入的密码不一致");
        }
        if (!agreeProtocol.equals("1")) {
            throw new BussinessException("请勾选同意《注册协议》");
        }
        //校验是否存在
        Users users = registLoginInterface.findByUsername(username);
        if (null != users) {
            return fail("用户已存在");
        }
        //校验验证码是否正确
        ResultEntity resultEntity;
        if (username.contains("@")) {
            resultEntity = registLoginInterface.checkCodeForRegist(username, verifyCode, Constants.REGIST_CODE_EMAIL);
        } else {
            resultEntity = registLoginInterface.checkCodeForRegist(username, verifyCode, Constants.REGIST_CODE_MOBILE);
        }
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return resultEntity;
        }
        //保存用户信息和用户设置
        resultEntity = registLoginInterface.register(username, encryptPwd);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return resultEntity;
        }
        users = JSON.parseObject(resultEntity.getData().toString(), Users.class);
        //初始化用户资产
        ResultEntity result = accountInterface.registerInit(users.getId());
        if (result.getCode() != ResultEntity.SUCCESS) {
            return result;
        }
        registLoginInterface.sendLetter(users.getId(), MsgTempNID.REGISTER_SUCCESS);
        //分配充币地址队列
        userCorrelationHandlerProducer.sendUserDistributeTask(users);
        return ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "手机号码/邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "graphicCodeKey", value = "图文验证码key", required = true, dataType = "String")
            , @ApiImplicitParam(name = "graphicCode", value = "用户输入的图文验证码", required = true, dataType = "String")
    })
    @ApiOperation(value = "获取短信或邮箱验证码-pc", notes = "通过用户填入的手机号码或邮箱获取验证码-适用于pc端，pc端需要先校验图文验证码")
    @RequestMapping("/getVerifyCodeForPc")
    public ResultEntity getRegistCodeForPc(@RequestParam String username, @RequestParam String graphicCodeKey, @RequestParam String graphicCode) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(graphicCodeKey) || StringUtils.isBlank(graphicCode)) {
            throw new BussinessException("参数错误");
        }
        //校验是否存在
        Users users = registLoginInterface.findByUsername(username);
        if (null != users) {
            return fail("用户已存在");
        }
        ResultEntity result = registLoginInterface.checkGraphicCode(graphicCodeKey, graphicCode);
        if (result.getCode() != ResultEntity.SUCCESS) {
            return result;
        }
        //发送验证码
        if (username.contains("@")) {
            registLoginInterface.sendEmailOrSms(username, Constants.REGIST_CODE_EMAIL);
        } else {
            registLoginInterface.sendEmailOrSms(username, Constants.REGIST_CODE_MOBILE);
        }
        return ok();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "手机号码/邮箱", required = true, dataType = "String")
    })
    @ApiOperation(value = "获取短信或邮箱验证码-app", notes = "通过用户填入的手机号码或邮箱获取验证码-适用于app端")
    @RequestMapping("/getVerifyCodeForApp")
    public ResultEntity getRegistCodeForApp(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            throw new BussinessException("参数错误");
        }
        //校验是否存在
        Users users = registLoginInterface.findByUsername(username);
        if (null != users) {
            return fail("用户已存在");
        }
        //发送验证码
        if (username.contains("@")) {
            registLoginInterface.sendEmailOrSms(username, Constants.REGIST_CODE_EMAIL);
        } else {
            registLoginInterface.sendEmailOrSms(username, Constants.REGIST_CODE_MOBILE);
        }
        return fail("发送验证码失败");
    }


    @ApiOperation(value = "获取图文验证码", notes = "获取图文验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "图文验证码key", required = true, dataType = "String")
    })
    @RequestMapping("/getImageCode")
    public ResultEntity getImageCode(@RequestParam String uuid) throws Exception {
        String captcha = new CaptchaHelper().createCaptcha();
        ResultEntity resultEntity = registLoginInterface.saveCode(CacheID.GRAPHIC_CODE_PREFIX + uuid, captcha);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok();
        } else {
            return fail(resultEntity.getMessage());
        }
    }


    @ApiOperation(value = "找回密码获取短信验证码", notes = "找回密码获取短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "graphicCodeKey", value = "图文验证码key", required = true, dataType = "String")
            , @ApiImplicitParam(name = "graphicCode", value = "用户输入的图文验证码", required = true, dataType = "String")
    })
    @RequestMapping("/getBackPwdCodeByMobile")
    public ResultEntity getBackPwdCodeByMobile(@RequestParam String mobile, @RequestParam String graphicCodeKey, @RequestParam String graphicCode) {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(graphicCodeKey) || StringUtils.isBlank(graphicCode)) {
            throw new BussinessException("参数错误");
        }
        //校验手机号码格式是否正确
        if (!ValidateUtils.isPhone(mobile)) {
            throw new BussinessException("手机号码格式错误");
        }
        //图文验证码校验
        ResultEntity result = registLoginInterface.checkGraphicCode(graphicCodeKey, graphicCode);
        if (result.getCode() != ResultEntity.SUCCESS) {
            return result;
        }
        //发送验证码
        registLoginInterface.sendEmailOrSms(mobile, Constants.BACK_PWD_MOBILE);
        return ok();
    }


    @ApiOperation(value = "找回密码获取短信验证码-APP", notes = "找回密码获取短信验证码-APP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String")
    })
    @RequestMapping("/getBackPwdCodeByMobileForApp")
    public ResultEntity getBackPwdCodeByMobileForApp(@RequestParam String mobile) {
        if (StringUtils.isBlank(mobile)) {
            throw new BussinessException("参数错误");
        }
        //校验手机号码格式是否正确
        if (!ValidateUtils.isPhone(mobile)) {
            throw new BussinessException("手机号码格式错误");
        }
        Users users = userInterface.selectByMobile(mobile);
        if (null == users) {
            return fail("该手机号码未注册");
        }
        //发送验证码
        registLoginInterface.sendEmailOrSms(mobile, Constants.BACK_PWD_MOBILE);
        return ok();
    }


    @ApiOperation(value = "找回密码获取邮箱验证码", notes = "找回密码获取邮箱验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "graphicCodeKey", value = "图文验证码key", required = true, dataType = "String")
            , @ApiImplicitParam(name = "graphicCode", value = "用户输入的图文验证码", required = true, dataType = "String")
    })
    @RequestMapping("/getBackPwdCodeByEmail")
    public ResultEntity getBackPwdCodeByEmail(@RequestParam String email, @RequestParam String graphicCodeKey, @RequestParam String graphicCode) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(graphicCodeKey) || StringUtils.isBlank(graphicCode)) {
            throw new BussinessException("参数错误");
        }
        //校验邮箱格式是否正确
        if (!ValidateUtils.isEmail(email)) {
            throw new BussinessException("邮箱格式错误");
        }

        Users users = userInterface.selectByEmail(email);
        if (null == users) {
            return fail("该邮箱未注册");
        }
        //图文验证码校验
        ResultEntity result = registLoginInterface.checkGraphicCode(graphicCodeKey, graphicCode);
        if (result.getCode() != ResultEntity.SUCCESS) {
            return result;
        }
        registLoginInterface.sendEmailOrSms(email, Constants.BACK_PWD_EMAIL);
        return ok();
    }

    @ApiOperation(value = "找回密码获取邮箱验证码-APP", notes = "找回密码获取邮箱验证码-APP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
    })
    @RequestMapping("/getBackPwdCodeByEmailForApp")
    public ResultEntity getBackPwdCodeByEmailForApp(@RequestParam String email) {
        if (StringUtils.isBlank(email)) {
            throw new BussinessException("参数错误");
        }
        //校验邮箱格式是否正确
        if (!ValidateUtils.isEmail(email)) {
            throw new BussinessException("邮箱格式错误");
        }
        Users users = userInterface.selectByEmail(email);
        if (null == users) {
            return fail("该邮箱未注册");
        }
        //发送验证码
        registLoginInterface.sendEmailOrSms(email, Constants.BACK_PWD_EMAIL);
        return ok();
    }


    @ApiOperation(value = "找回密码下一步-邮箱", notes = "找回密码下一步-邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "code", value = "邮箱验证码", required = true, dataType = "String")
    })
    @RequestMapping("/nextBackPwdByEmail")
    public ResultEntity nextBackPwdByEmail(@RequestParam String email,
                                           @RequestParam String code) {
        //1.校验验证码是否正确
        ResultEntity resultEntity = registLoginInterface.checkCodeForBackPwd(email, code, Constants.BACK_PWD_EMAIL);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return fail("验证码错误");
        }
        //2.校验用户名是否存在
        Users users = userInterface.selectByEmail(email);
        if (null == users) {
            return fail("邮箱不存在");
        }
        //生成一个authKey回传到前台，用于鉴定重设密码接口是否允许执行
        String s = RandomUtils.randomCustomUUID();
        registLoginInterface.saveCode(CacheID.BACK_PWD_KEY_PREFIX + email, s);
        return ok(s);
    }

    @ApiOperation(value = "找回密码下一步-手机号码", notes = "找回密码下一步-手机号码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String")
            , @ApiImplicitParam(name = "code", value = "短信验证码", required = true, dataType = "String")
    })
    @RequestMapping("/nextBackPwdByMobile")
    public ResultEntity nextBackPwdByMobile(@RequestParam String mobile,
                                            @RequestParam String code) {
        //1.校验验证码是否正确
        ResultEntity resultEntity = registLoginInterface.checkCodeForBackPwd(mobile, code, Constants.BACK_PWD_MOBILE);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return fail("验证码错误");
        }
        //2.校验用户名是否存在
        Users users = userInterface.selectByMobile(mobile);
        if (null == users) {
            return fail("手机号码不存在");
        }
        //生成一个authKey回传到前台，用于鉴定重设密码接口是否允许执行
        String s = RandomUtils.randomCustomUUID();
        registLoginInterface.saveCode(CacheID.BACK_PWD_KEY_PREFIX + mobile, s);
        return ok(s);
    }


    @ApiOperation(value = "找回密码", notes = "找回密码下一步-手机号码/邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "手机号/邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "authKey", value = "authKey", required = true, dataType = "String")
            , @ApiImplicitParam(name = "newPwd", value = "加密的新密码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "newConfirmPwd", value = "加密的二次新密码", required = true, dataType = "String")
    })
    @RequestMapping("/doResetBackPwd")
    public ResultEntity doResetBackPwd(@RequestParam String username,
                                       @RequestParam String authKey,
                                       @RequestParam String newPwd,
                                       @RequestParam String newConfirmPwd) throws Exception {
        //1.校验参数
        if (StrUtil.isBlank(username) || StrUtil.isBlank(authKey) || StrUtil.isBlank(newPwd) || StrUtil.isBlank(newConfirmPwd)) {
            return fail("参数错误");
        }
        if (!RSAUtils.decrypt(newPwd).equals(RSAUtils.decrypt(newConfirmPwd))) {
            return fail("两次输入的密码不一致");
        }
        ResultEntity resultEntity = registLoginInterface.doResetBackPwd(username, authKey, newPwd, newConfirmPwd);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return resultEntity;
        }
        return ok();
    }

    @ApiOperation(value = "获取注册协议", notes = "获取注册协议")
    @RequestMapping("/getRegistProtocol")
    public ResultEntity getRegistProtocol() {
        return userInterface.getRegistProtocol();
    }

    @ApiOperation(value = "获取风险提示协议", notes = "获取风险提示协议")
    @RequestMapping("/getRiskProtocol")
    public ResultEntity getRiskProtocol() {
        return userInterface.getRiskProtocol();
    }

}
