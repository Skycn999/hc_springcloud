/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.user.controller;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.*;
import com.mi.hundsun.oxchains.base.core.auth.google.GoogleAuthenticator;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.constant.CacheID;
import com.mi.hundsun.oxchains.base.core.constant.MsgTempNID;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.base.core.service.sms.SendMsgService;
import com.mi.hundsun.oxchains.base.core.service.user.RegistLoginService;
import com.mi.hundsun.oxchains.base.core.service.user.UserInLetterService;
import com.mi.hundsun.oxchains.base.core.service.user.UsersService;
import com.mi.hundsun.oxchains.provider.user.rabbitmq.SysSmsMessageProducer;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

/**
 * @author 枫亭
 * @date 2018-04-08 20:58.
 */
@Api(value = "注册登录模块服务", description = "RegistLoginController Created By 枫亭 at 2018-04-08 20:58")
@RestController
@RequestMapping("/prod/registLogin")
public class RegistLoginController extends GenericController {

    @Autowired
    UsersService usersService;
    @Autowired
    RedisService redisService;
    @Autowired
    RegistLoginService registLoginService;
    @Autowired
    SendMsgService sendMsgService;
    @Autowired
    UserInLetterService userInLetterService;
    @Autowired
    SysSmsMessageProducer sendMessageProducer;


    @PostMapping("/sendEmailOrSms")
    public void sendEmailOrSms(@RequestParam String username, @RequestParam String type) {
        if (username.contains("@")) {
            sendMessageProducer.sendEmail(username, type);
        } else {
            sendMessageProducer.sendSms(username, type);
        }
    }

    @PostMapping("/sendLetter")
    public void sendLetter(@RequestParam Integer userId, @RequestParam String nid) {
        sendMessageProducer.sendLetter(userId, nid);
    }


    @PostMapping("/sendLetter2")
    public void sendLetter2(@RequestParam Integer userId,@RequestParam String currency,@RequestParam String num, @RequestParam String nid) {
        sendMessageProducer.sendLetter2(userId,currency,num,nid);
    }

    @PostMapping("/findByUsername")
    public Users findByUsername(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            throw new BussinessException("parameters can not empty");
        }
        Users user;
        //判断用户使用手机号码登录还是邮箱登录
        if (username.contains("@")) {
            //邮箱登录
            user = usersService.selectByEmail(username);
        } else {
            user = usersService.selectByMobile(username);
        }
        return user;
    }

    @PostMapping("/updateLastLogin")
    public ResultEntity updateLastLogin(@RequestParam Integer userId) {
        try {
            usersService.updateByPrimaryKeySelective(new Users(u -> {
                u.setId(userId);
                u.setLastLoginDate(new Date());
            }));
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    @PostMapping("/validLoginInfo")
    public ResultEntity validLoginInfo(@RequestParam String username,
                                       @RequestParam String encryptPwd)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(encryptPwd)) {
            throw new BussinessException("parameters can not empty");
        }
        Users user = usersService.selectByUserName(username);
        if (null == user) {
            throw new BussinessException("用户名或密码错误");
        }
        return registLoginService.validLoginInfo(user, encryptPwd) ? ok("登录成功") : fail("登录失败");
    }

    @PostMapping("/checkSafeAuthForLogin")
    public ResultEntity checkSafeAuthForLogin(@RequestParam String username, @RequestParam String authKey,
                                              @RequestParam Integer googleState, @RequestParam String googleCode) {
        try {
            //1.校验授权码
            String s = redisService.get(CacheID.LOGIN_KEY_PREFIX + username);
            if (!authKey.equals(s)) {
                return fail("登录失败");
            }
            Users user = usersService.selectByUserName(username);
            if (null == user) {
                return fail("登录失败");
            }
            if (googleState.intValue() == user.getGoogleState()) {
                if (googleState == Users.GOOGLESTATE.open.code && !StringUtils.isBlank(user.getGoogleKey())) {
                    //解密
                    String googleKey = ToolAES.decrypt(user.getGoogleKey());
                    //校验Google授权码
                    if (!GoogleAuthenticator.verify(googleCode, googleKey)) {
                        return fail("Google授权码校验失败");
                    }
                }
            } else {
                return fail("用户已开启Google验证,请输入Google验证码");
            }
            //删除授权码
            redisService.del(CacheID.LOGIN_KEY_PREFIX + username);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    /**
     * 通过邮箱找回密码 - 校验用户输入的验证码是否正确
     *
     * @param username 邮箱
     * @param code     验证码
     * @return ResultEntity
     */
    @PostMapping("/checkCodeForRegist")
    public ResultEntity checkCodeForRegist(@RequestParam String username,
                                           @RequestParam String code,
                                           @RequestParam String type) {
        //判断图文验证码是否正确
        boolean b = registLoginService.checkSmsOrEmailCode(username, code, type);
        if (!b) {
            if (username.contains("@")) {
                return fail("邮箱验证码错误");
            } else {
                return fail("短信验证码错误");
            }

        }
        return ok();
    }


    @PostMapping("/saveCode")
    public ResultEntity saveCode(@RequestParam String key,
                                 @RequestParam String code) {
        //保存随机码到redis中
        try {
            redisService.put(key, code, 600);
        } catch (Exception e) {
            fail("保存数据到缓存出错");
        }
        return ok();
    }

    @PostMapping(value = "/register")
    public ResultEntity register(@RequestParam String username, @RequestParam String encryptPwd) {
        if (StrUtil.isBlank(username) || StrUtil.isBlank(encryptPwd)) {
            return fail("[用户名]不能为空");
        }
        if (!ValidateUtils.isEmail(username) && !ValidateUtils.isPhone(username)) {
            return fail("[用户名]格式错误");
        }
        try {
            Users user = registLoginService.register(username, encryptPwd);
            return ok(JSON.toJSONString(user));
        } catch (BussinessException e) {
            e.printStackTrace();
            return fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    @PostMapping("/checkGraphicCode")
    public ResultEntity checkGraphicCode(@RequestParam String graphicCodeKey,
                                         @RequestParam String graphicCode) {
        if (StrUtil.isBlank(graphicCodeKey) || StrUtil.isBlank(graphicCode)) {
            return fail("必填参数不能为空");
        }
        boolean b = registLoginService.checkGraphicCode(graphicCodeKey, graphicCode);
        return b ? ok() : fail("图文验证码错误");
    }

    @PostMapping("/getRsaPublicKey")
    public ResultEntity getRsaPublicKey() {
        return ok(RSAUtils.getPublicKeyObj());
    }


    /**
     * 通过手机/邮箱 - 校验用户输入的验证码是否正确
     *
     * @param username 手机号码/邮箱
     * @param code     验证码
     * @param type     类型
     * @return ResultEntity
     */
    @PostMapping("/checkCodeForBackPwd")
    public ResultEntity checkCodeForBackPwd(@RequestParam String username,
                                            @RequestParam String code,
                                            @RequestParam String type) {
        //判断图文验证码是否正确
        boolean b = registLoginService.checkSmsOrEmailCode(username, code, type);
        if (!b) {
            throw new BussinessException("验证码错误");
        }
        return ok();
    }

    /**
     * 执行找回登录密码
     *
     * @param username      邮箱/手机号码
     * @param authKey       授权码
     * @param newPwd        新密码
     * @param newConfirmPwd 确认密码
     * @return ResultEntity
     */
    @PostMapping("/doResetBackPwd")
    public ResultEntity doResetBackPwd(@RequestParam String username,
                                       @RequestParam String authKey,
                                       @RequestParam String newPwd,
                                       @RequestParam String newConfirmPwd) throws Exception {
        if (StrUtil.isBlank(username) || StrUtil.isBlank(authKey) || StrUtil.isBlank(newPwd) || StrUtil.isBlank(newConfirmPwd)) {
            return fail("必填参数不能为空");
        }
        //1.校验授权码
        String s = redisService.get(CacheID.BACK_PWD_KEY_PREFIX + username);
        if (!authKey.equals(s)) {
            throw new BussinessException("非法操作或验证码已过期");
        }
        //删除授权码
        redisService.del(CacheID.BACK_PWD_KEY_PREFIX + username);
     /*   if(!newPwd.equals(newConfirmPwd)) {
            throw new BussinessException("两次输入的密码不一致");
        }*/
        //解密密码
        String pwd = RSAUtils.decrypt(newPwd);
        Users users = usersService.selectByUserName(username);
        if (null == users) {
            throw new BussinessException("用户不存在");
        }
        //重设密码
        String salt = MD5Utils.generateSalt();
        users.setPwd(MD5Utils.getMd5(pwd, salt));
        users.setPwdSalt(salt);
        users.setUpdateTime(new Date());
        users.setUpdator(username);
        //更新用户信息
        usersService.updateByPrimaryKeySelective(users);
        //发送站内信
        sendMessageProducer.sendLetter(users.getId(), MsgTempNID.RETRIEVE_LOGIN_PWD);
        return ok();
    }

}
