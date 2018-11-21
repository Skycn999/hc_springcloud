package com.mi.hundsun.oxchains.provider.user.controller;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.*;
import com.mi.hundsun.oxchains.base.core.auth.google.GoogleAuthenticator;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.constant.CacheID;
import com.mi.hundsun.oxchains.base.core.constant.CoinCode;
import com.mi.hundsun.oxchains.base.core.constant.Constants;
import com.mi.hundsun.oxchains.base.core.constant.MsgTempNID;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.user.UserAddress;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.base.core.service.user.*;
import com.mi.hundsun.oxchains.provider.user.rabbitmq.SysSmsMessageProducer;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author db
 * @date 2018-04-08 20:49.
 */
@Api(value = "账户设置相关服务", description = "AccountSetController Created By db At 2018-04-08 20:49.")
@Slf4j
@RestController
@RequestMapping("/prod/account/set")
public class AccountSetController extends GenericController {

    @Autowired
    UsersService usersService;
    @Autowired
    UserIdentifyService userIdentifyService;
    @Autowired
    UserAddressService userAddressService;
    @Autowired
    RedisService redisService;
    @Autowired
    RegistLoginService registLoginService;
    @Autowired
    UserInLetterService userInLetterService;
    @Autowired
    PlatUserAddressController platUserAddressController;
    @Autowired
    SysSmsMessageProducer sendMessageProducer;

    @ApiOperation(value = "账户设置信息", notes = "获取账户设置信息-如" +
            "账户等级(1-低,2-中,3-高,4-非常高); 各认证类型的状态。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer")
    })
    @PostMapping("/myAccountSetInfo")
    public ResultEntity myAccountSetInfo(@RequestParam Integer userId) {
        if (null == userId) {
            return fail("用户id参数错误");
        }
        try {
            Users users = usersService.selectOne(new Users(u -> {
                u.setId(userId);
                u.setDelFlag(GenericPo.DELFLAG.NO.code);
            }));
            if (null == users) {
                return fail();
            }
            UserIdentify userIdentify = userIdentifyService.selectOne(new UserIdentify(u -> {
                u.setUserId(userId);
                u.setDelFlag(GenericPo.DELFLAG.NO.code);
            }));
            if (null == userIdentify) {
                return fail();
            }
            List<UserAddress> userAddressList = userAddressService.select(new UserAddress(ua -> {
                ua.setUserId(userId);
                ua.setDelFlag(GenericPo.DELFLAG.NO.code);
            }));
            //安全等级
            int safeGrade = 0;
            boolean mobileState = false;
            boolean emailState = false;
            boolean loginPwdState = false;
            boolean mentionPwdState = false;
            boolean googleAuth = false;
            Integer realnameState;
            boolean userAddress = false;
            String realName = "";
            String idNo = "";

            Map<String, Object> map = new HashMap<>();
            if (userIdentify.getMobileState() == UserIdentify.MOBILESTATE.CERTIFIED.code) {
                mobileState = true;
                String mobile = hideUserName(users.getMobile(), Constants.MOBILE_TYPE);
                map.put("mobile", mobile);
            }
            if (userIdentify.getEmailState() == UserIdentify.EMAILSTATE.CERTIFIED.code) {
                String email = hideUserName(users.getEmail(), Constants.EMAIL_TYPE);
                map.put("email", email);
                emailState = true;
            }
            realnameState = userIdentify.getRealnameState();
            if (userIdentify.getRealnameState() == UserIdentify.REALNAMESTATE.CERTIFIED.code) {
                realName = hideRealName(users.getRealname());
                idNo = hideIdNo(users.getIdNo());
                safeGrade++;
            }
            if (StringUtils.isNotBlank(users.getPwd())) {
                loginPwdState = true;
            }
            if (StringUtils.isNotBlank(users.getMentionPwd())) {
                mentionPwdState = true;
                safeGrade++;
            }
            if (StringUtils.isNotBlank(users.getGoogleKey())) {
                googleAuth = true;
                safeGrade++;
            }
            if (mobileState || emailState || loginPwdState) {
                safeGrade++;
            }
            if (null != userAddressList && userAddressList.size() > 0) {
                userAddress = true;
            }
            map.put("mobileState", mobileState);//值为未绑定或者已绑定+打星号的手机号码
            map.put("emailState", emailState);//值为未绑定或者已绑定+打星号的邮箱
            map.put("loginPwdState", loginPwdState);//值为已设置或未设置
            map.put("mentionPwdState", mentionPwdState);//值为已设置或未设置
            map.put("googleAuth", googleAuth);//谷歌验证
            map.put("googleState", users.getGoogleState());//谷歌验证器状态
            map.put("realnameState", realnameState);//实名认证
            map.put("realName", realName);//真实姓名
            map.put("idNo", idNo);//证件号
            map.put("userAddress", userAddress);//用户地址
            map.put("safeGrade", safeGrade);//安全等级 (1-低,2-中,3-高,4-非常高)
            return ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    /**
     * 隐藏用户名
     *
     * @param userName 用户名
     * @param type     1、手机号, 2、邮箱
     * @return hideUserName
     */
    private String hideUserName(String userName, int type) {
        String hideUserName = "";
        if (type == Constants.MOBILE_TYPE) {
            hideUserName = userName.substring(0, 3) + "****" + userName.substring(7, userName.length());
        } else if (type == Constants.EMAIL_TYPE) {
            hideUserName = userName.substring(0, 2) + "****" + userName.substring(userName.lastIndexOf("@"), userName.length());
        }
        return hideUserName;
    }


    /**
     * 隐藏真实姓名
     */
    private String hideRealName(String realName) {
        String hideRealName = "";
        if (realName.length() > 0) {
            hideRealName = realName.substring(0, 1) + "**";
        }
        return hideRealName;
    }

    /**
     * 隐藏证件号码
     */
    private String hideIdNo(String idNo) {
        String hideIdNo = "";
        if (idNo.length() > 0) {
            hideIdNo = idNo.substring(0, 4) + "****" + idNo.substring(idNo.length() - 3, idNo.length());
        }
        return hideIdNo;
    }


    @ApiOperation(value = "绑定手机号码", notes = "绑定手机号码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String")
    })
    @PostMapping("/bindMobile")
    public ResultEntity bindMobile(@RequestParam Integer userId, @RequestParam String mobile) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(mobile)) {
            fail("必填参数不能为空");
        }
        try {
            usersService.bindMobile(userId, mobile);
            sendMessageProducer.sendLetter(userId, MsgTempNID.BIND_MOBILE);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }


    @ApiOperation(value = "绑定邮箱", notes = "绑定邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
    })
    @PostMapping("/bindEmail")
    public ResultEntity bindEmail(@RequestParam Integer userId, @RequestParam String email) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(email)) {
            fail("必填参数不能为空");
        }
        try {
            usersService.bindEmail(userId, email);
            //发送站内信
            sendMessageProducer.sendLetter(userId, MsgTempNID.BIND_EMAIL);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    /**
     * 执行修改登录密码
     *
     * @param username      邮箱/手机号码
     * @param authKey       授权码
     * @param newPwd        新密码
     * @param newConfirmPwd 确认密码
     * @return ResultEntity
     */
    @PostMapping("/doModifyLoginPwd")
    public ResultEntity doModifyLoginPwd(@RequestParam String username,
                                         @RequestParam String authKey,
                                         @RequestParam String newPwd,
                                         @RequestParam String newConfirmPwd) throws Exception {
        if (StrUtil.isBlank(username) || StrUtil.isBlank(authKey) || StrUtil.isBlank(newPwd) || StrUtil.isBlank(newConfirmPwd)) {
            return fail("必填参数不能为空");
        }
        //1.校验授权码
        String s = redisService.get(CacheID.MODIFY_LOGIN_PWD_KEY_PREFIX + username);
        if (!authKey.equals(s)) {
            throw new BussinessException("非法操作或验证码已过期");
        }
        //删除授权码
        redisService.del(CacheID.MODIFY_LOGIN_PWD_KEY_PREFIX + username);

        if (!RSAUtils.decrypt(newPwd).equals(RSAUtils.decrypt(newConfirmPwd))) {
            return fail("两次密码不一致");
        }
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
        sendMessageProducer.sendLetter(users.getId(), MsgTempNID.MODIFY_LOGIN_PWD);
        return ok();
    }

    @ApiOperation(value = "我的提币地址列表", notes = "我的提币地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
    })
    @PostMapping("getUserAddressList")
    public ResultEntity getUserAddressList(@RequestParam Integer userId, @RequestParam Integer pageSize,
                                           @RequestParam Integer pageNumber) {
        if (null == pageSize) {
            return fail("分页条数参数错误");
        }
        if (null == pageNumber) {
            return fail("分页页码参数错误");
        }
        if (null == userId) {
            return fail("用户id参数错误");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            long recordCount = userAddressService.getUserAddressCount(userId);
            //总分页数
            int pageNum = (int) recordCount / pageSize + (recordCount % pageSize > 0 ? 1 : 0);
            List<UserAddress> list = userAddressService.getUserAddressList(userId, pageSize, pageNumber);
            map.put("recordCount", recordCount);
            map.put("pageNum", pageNum);
            map.put("data", list);
            return ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }


    @ApiOperation(value = "我的提币地址列表-APP", notes = "我的提币地址列表-APP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "code", value = "币种", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
    })
    @PostMapping("getUserAddressListForApp")
    public ResultEntity getUserAddressListForApp(@RequestParam Integer userId, @RequestParam String code, @RequestParam Integer pageSize,
                                                 @RequestParam Integer pageNumber) {
        if (null == pageSize) {
            return fail("分页条数参数错误");
        }
        if (null == pageNumber) {
            return fail("分页页码参数错误");
        }
        if (null == userId) {
            return fail("用户id参数错误");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            long recordCount = userAddressService.getUserAddressCount(userId, code);
            //总分页数
            int pageNum = (int) recordCount / pageSize + (recordCount % pageSize > 0 ? 1 : 0);
            List<UserAddress> list = userAddressService.getUserAddressList(userId, code, pageSize, pageNumber);
            map.put("recordCount", recordCount);
            map.put("pageNum", pageNum);
            map.put("data", list);
            return ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }


    @ApiOperation(value = "我的提币地址数量统计-APP", notes = "我的提币地址数量统计-APP")
    @ApiImplicitParams({
    })
    @PostMapping("getUserAddressSizeForApp")
    public ResultEntity getUserAddressSizeForApp(Integer userId) {
        try {
            Map<String, Object> map = new HashMap<>();
            long btcSize = userAddressService.getUserAddressSize(userId, CoinCode.BTC);
            map.put("BTC", btcSize);
            long etcSize = userAddressService.getUserAddressSize(userId, CoinCode.ETH);
            map.put("ETH", etcSize);
            return ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }


    @ApiOperation(value = "添加提币地址", notes = "添加提币地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "code", value = "币种", required = true, dataType = "String")
            , @ApiImplicitParam(name = "address", value = "地址", required = true, dataType = "String")
            , @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "String")
            , @ApiImplicitParam(name = "isDefault", value = "是否默认0,否 1,是", required = true, dataType = "Integer")
    })
    @PostMapping("addUserAddress")
    public ResultEntity addUserAddress(@RequestParam Integer userId,
                                       @RequestParam String code,
                                       @RequestParam String address,
                                       @RequestParam String remark,
                                       @RequestParam Integer isDefault) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(code) || StringUtils.isBlank(address) || StringUtils.isBlank(remark) || StringUtils.isBlank(isDefault)) {
            return fail("必填参数不能为空");
        }
        try {
            UserAddress userAddress = new UserAddress();
            userAddress.setUuid(RandomUtils.randomCustomUUID());
            userAddress.setUserId(userId);
            userAddress.setCoinCurrency(code);
            userAddress.setAddress(address);
            userAddress.setIsDefault(isDefault);
            userAddress.setRemark(remark);
            userAddress.setDelFlag(GenericPo.DELFLAG.NO.code);
            userAddress.setCreateTime(new Date());
            userAddressService.insertAddress(userAddress);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }


    @ApiOperation(value = "删除提币地址", notes = "删除提币地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "idStr", value = "提币地址idStr", required = true, dataType = "Integer")
    })
    @PostMapping("delUserAddressForApp")
    public ResultEntity delUserAddressForApp(@RequestParam Integer userId, @RequestParam String idStr) {
        if (null == userId || 0 == userId) {
            throw new BussinessException("userId不能为空");
        }
        try {
            int i = userAddressService.delUserAddressForApp(userId, idStr);
            if (i > 0) {
                return ok();
            }
            return fail("删除提币地址失败");
        } catch (Exception e) {
            e.printStackTrace();
            return fail("删除提币地址失败");
        }
    }


    @ApiOperation(value = "删除提币地址", notes = "删除提币地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "id", value = "提币地址ID", required = true, dataType = "Integer")
    })
    @PostMapping("delUserAddress")
    public ResultEntity delUserAddress(@RequestParam Integer userId, @RequestParam Integer id) {
        try {
            int i = userAddressService.delUserAddress(userId, id);
            if (i < 1) {
                return fail("删除失败");
            }
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail("删除失败");
        }
    }


    @ApiOperation(value = "设置提币密码", notes = "设置提币密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "newPwd", value = "加密后的密码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "newConfirmPwd", value = "加密后的密码", required = true, dataType = "String")
    })
    @PostMapping("setMentionPwd")
    public ResultEntity setMentionPwd(@RequestParam Integer userId, @RequestParam String newPwd, String newConfirmPwd) throws Exception {
        if (!RSAUtils.decrypt(newPwd).equals(RSAUtils.decrypt(newConfirmPwd))) {
            return fail("两次密码不一致");
        }
        //解密密码
        String pwd = RSAUtils.decrypt(newPwd);
        Users users = usersService.selectOne(new Users(u -> u.setId(userId)));
        if (null == users) {
            return fail("用户不存在");
        }
        String salt = MD5Utils.generateSalt();
        users.setMentionPwd(MD5Utils.getMd5(pwd, salt));
        users.setMentionPwdSalt(salt);
        users.setUpdateTime(new Date());
        //更新用户信息
        usersService.updateByPrimaryKeySelective(users);
        sendMessageProducer.sendLetter(userId, MsgTempNID.SET_MENTION_COIN_PWD);
        return ok();
    }


    @ApiOperation(value = "修改提币密码", notes = "修改提币密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "newPwd", value = "加密后的密码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "newConfirmPwd", value = "加密后的密码", required = true, dataType = "String")
    })
    @PostMapping("doModifyMentionPwd")
    public ResultEntity doModifyMentionPwd(@RequestParam Integer userId, @RequestParam String newPwd, String newConfirmPwd) throws Exception {
        if (!RSAUtils.decrypt(newPwd).equals(RSAUtils.decrypt(newConfirmPwd))) {
            return fail("两次密码不一致");
        }
        //解密密码
        String pwd = RSAUtils.decrypt(newPwd);
        Users users = usersService.selectOne(new Users(u -> u.setId(userId)));
        if (null == users) {
            return fail("用户不存在");
        }
        String salt = MD5Utils.generateSalt();
        users.setMentionPwd(MD5Utils.getMd5(pwd, salt));
        users.setMentionPwdSalt(salt);
        users.setUpdateTime(new Date());
        //更新用户信息
        usersService.updateByPrimaryKeySelective(users);
        sendMessageProducer.sendLetter(userId, MsgTempNID.MODIFY_MENTION_COIN_PWD);
        return ok();
    }


    @ApiOperation(value = "找回提币密码", notes = "找回提币密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "username", value = "手机号/邮箱", required = true, dataType = "String")
            , @ApiImplicitParam(name = "authKey", value = "authKey", required = true, dataType = "String")
            , @ApiImplicitParam(name = "newPwd", value = "加密后的密码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "newConfirmPwd", value = "加密后的密码", required = true, dataType = "String")
    })
    @PostMapping("doResetBackMentionPwd")
    public ResultEntity doResetBackMentionPwd(@RequestParam Integer userId, @RequestParam String username, @RequestParam String authKey, @RequestParam String newPwd, String newConfirmPwd) throws Exception {
        if (!RSAUtils.decrypt(newPwd).equals(RSAUtils.decrypt(newConfirmPwd))) {
            return fail("两次密码不一致");
        }
        //1.校验授权码
        String s = redisService.get(CacheID.BACK_PWD_KEY_PREFIX + username);
        if (!authKey.equals(s)) {
            throw new BussinessException("非法操作或验证码已过期");
        }
        //删除授权码
        redisService.del(CacheID.BACK_PWD_KEY_PREFIX + username);
        //解密密码
        String pwd = RSAUtils.decrypt(newPwd);
        Users users = usersService.selectOne(new Users(u -> u.setId(userId)));
        if (null == users) {
            return fail("用户不存在");
        }
        String salt = MD5Utils.generateSalt();
        users.setMentionPwd(MD5Utils.getMd5(pwd, salt));
        users.setMentionPwdSalt(salt);
        users.setUpdateTime(new Date());
        //更新用户信息
        usersService.updateByPrimaryKeySelective(users);
        sendMessageProducer.sendLetter(userId, MsgTempNID.RETRIEVE_MENTION_COIN_PWD);
        return ok();
    }


    @ApiOperation(value = "生成Google私钥", notes = "用户绑定谷歌验证器接口-返回key和二维码地址和手机号/邮箱")
    @PostMapping(value = "/generateGoogleAuthKey")
    public ResultEntity generateGoogleAuthKey(@RequestParam String uuid) {
        if (StrUtil.isBlank(uuid)) {
            return fail("uuid不能为空");
        }
        Users user = usersService.selectByUuid(uuid);
        //1.生成秘钥保存到redis
        String googleKey = GoogleAuthenticator.generateSecretKey();
        if (!StrUtil.isBlank(googleKey)) {
            //保存到redis 30分钟
            redisService.put(CacheID.BIND_GOOGLE_KEY + uuid, googleKey, 1800);
        } else {
            return fail("生成Google私钥失败");
        }
        String username;
        if (StrUtil.isBlank(user.getEmail())) {
            username = user.getMobile();
        } else {
            username = user.getEmail();
        }
        long s1 = System.currentTimeMillis();
        //2.生成二维码地址
        String barcode = GoogleAuthenticator.getQRBarcode(username, googleKey);
        String qrBarcodeUrl = platUserAddressController.generateQrCode(barcode);
        System.out.println("上传图片耗时:" + (System.currentTimeMillis() - s1));
        //验证用户认证并返回用户的手机号码/邮箱
        UserIdentify userIdentify = userIdentifyService.selectOne(new UserIdentify(u -> {
            u.setUserId(user.getId());
            u.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        Map<String, Object> map = new HashMap<>();
        if (userIdentify.getMobileState() == UserIdentify.MOBILESTATE.CERTIFIED.code) {
            String hideUserName = hideUserName(user.getMobile(), Constants.MOBILE_TYPE);
            map.put("username", hideUserName);
            map.put("type", Constants.MOBILE_TYPE);
        } else if (userIdentify.getEmailState() == UserIdentify.EMAILSTATE.CERTIFIED.code) {
            String hideUserName = hideUserName(user.getEmail(), Constants.EMAIL_TYPE);
            map.put("username", hideUserName);
            map.put("type", Constants.EMAIL_TYPE);
        }
        map.put("key", googleKey);
        map.put("qrCodePath", qrBarcodeUrl);
        return ok(map);
    }


    @ApiOperation(value = "校验验证码-绑定谷歌验证器-APP")
    @PostMapping(value = "/checkCodeForApp")
    public ResultEntity checkCodeForApp(@RequestParam String uuid, @RequestParam Integer type, @RequestParam String code) throws Exception {
        if (StringUtils.isBlank(uuid)) {
            return fail("uuid不能为空");
        }
        if (StringUtils.isBlank(type)) {
            return fail("type不能为空");
        }
        if (StringUtils.isBlank(code)) {
            return fail("code不能为空");
        }
        Users users = usersService.selectByUuid(uuid);
        boolean b;
        //校验短信验证码
        if (type == Constants.MOBILE_TYPE) {
            b = registLoginService.checkSmsOrEmailCode(users.getMobile(), code, Constants.BIND_GOOGLE_KEY_MOBILE);
        } else {
            b = registLoginService.checkSmsOrEmailCode(users.getEmail(), code, Constants.BIND_GOOGLE_KEY_EMAIl);
        }
        if (!b && type == Constants.MOBILE_TYPE) {
            return fail("短信验证码校验失败");
        } else if (!b && type == Constants.EMAIL_TYPE) {
            return fail("邮箱验证码校验失败");
        }
        //生成一个authKey回传到前台，用于鉴定重设密码接口是否允许执行
        String s = RandomUtils.randomCustomUUID();
        if (type == Constants.MOBILE_TYPE) {
            redisService.put(CacheID.BIND_GOOGLE_KEY_AUTH_KEY + users.getMobile(), s, 600);
        } else {
            redisService.put(CacheID.BIND_GOOGLE_KEY_AUTH_KEY + users.getEmail(), s, 600);
        }
        return ok(s);
    }

    @ApiOperation(value = "绑定Google验证码-APP")
    @PostMapping(value = "/bindGoogleAuthKeyForApp")
    public ResultEntity bindGoogleAuthKeyForApp(@RequestParam String uuid,
                                                @RequestParam Integer type,
                                                @RequestParam String authKey,
                                                @RequestParam String googleCode) throws Exception {
        if (StringUtils.isBlank(uuid)) {
            return fail("uuid不能为空");
        }
        if (StringUtils.isBlank(type)) {
            return fail("type不能为空");
        }
        if (StringUtils.isBlank(googleCode)) {
            return fail("googleCode不能为空");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("参数类型错误");
        }
        Users users = usersService.selectByUuid(uuid);
        String key = null;
        if (type == Constants.MOBILE_TYPE) {
            key = redisService.get(CacheID.BIND_GOOGLE_KEY_AUTH_KEY + users.getMobile());
        } else {
            key = redisService.get(CacheID.BIND_GOOGLE_KEY_AUTH_KEY + users.getEmail());
        }
        if (!key.equals(authKey)) {
            return fail("验证校验失败");
        }
        //获取goolge私钥
        String googleKey = redisService.get(CacheID.BIND_GOOGLE_KEY + uuid);
        if (StringUtils.isBlank(googleKey)) {
            return fail("google私钥不存在或已超时");
        }
        //校验Google授权码
        if (!GoogleAuthenticator.verify(googleCode, googleKey)) {
            return fail("Google授权码校验失败");
        }
        //googlekey加密
        String googleKeyAes = ToolAES.encrypt(googleKey);
        users.setGoogleKey(googleKeyAes);
        //2.更新用户信息
        usersService.updateUsers(new Users(u -> {
            u.setGoogleState(Users.GOOGLESTATE.open.code);
            u.setId(users.getId());
            u.setGoogleKey(googleKeyAes);
        }));
        //删除goolge私钥
        redisService.del(CacheID.BIND_GOOGLE_KEY + uuid);
        //删除验证
        if (type == Constants.MOBILE_TYPE) {
            redisService.del(CacheID.BIND_GOOGLE_KEY_AUTH_KEY + users.getMobile());
        } else {
            redisService.del(CacheID.BIND_GOOGLE_KEY_AUTH_KEY + users.getEmail());
        }
        sendMessageProducer.sendLetter(users.getId(), MsgTempNID.BIND_GOOGLE_KEY);
        return ok();
    }

    @ApiOperation(value = "绑定google验证码")
    @PostMapping(value = "/bindGoogleAuthKey")
    public ResultEntity bindGoogleAuthKey(@RequestParam String uuid,
                                          @RequestParam Integer type,
                                          @RequestParam String code,
                                          @RequestParam String googleCode) throws Exception {
        if (StringUtils.isBlank(uuid)) {
            return fail("uuid不能为空");
        }
        if (StringUtils.isBlank(type)) {
            return fail("type不能为空");
        }
        if (StringUtils.isBlank(code)) {
            return fail("code不能为空");
        }
        if (StringUtils.isBlank(googleCode)) {
            return fail("googleCode不能为空");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("参数类型错误");
        }
        Users users = usersService.selectByUuid(uuid);
        boolean b;
        //校验短信验证码
        if (type == Constants.MOBILE_TYPE) {
            b = registLoginService.checkSmsOrEmailCode(users.getMobile(), code, Constants.BIND_GOOGLE_KEY_MOBILE);
        } else {
            b = registLoginService.checkSmsOrEmailCode(users.getEmail(), code, Constants.BIND_GOOGLE_KEY_EMAIl);
        }
        if (!b && type == Constants.MOBILE_TYPE) {
            return fail("短信验证码校验失败");
        } else if (!b) {
            return fail("邮箱验证码校验失败");
        }
        //获取goolge私钥
        String googleKey = redisService.get(CacheID.BIND_GOOGLE_KEY + uuid);
        if (StringUtils.isBlank(googleKey)) {
            return fail("google私钥不存在或已超时");
        }
        //校验Google授权码
        if (!GoogleAuthenticator.verify(googleCode, googleKey)) {
            return fail("Google授权码校验失败");
        }
        if (null == googleKey) {
            return fail("googleKey生成失败");
        }
        //googleKey加密
        String googleKeyAes = ToolAES.encrypt(googleKey);
        users.setGoogleKey(googleKeyAes);
        //2.更新用户信息
        usersService.updateUsers(new Users(u -> {
            u.setGoogleState(Users.GOOGLESTATE.open.code);
            u.setId(users.getId());
            u.setGoogleKey(googleKeyAes);
        }));
        //删除google私钥
        redisService.del(CacheID.BIND_GOOGLE_KEY + uuid);
        sendMessageProducer.sendLetter(users.getId(), MsgTempNID.BIND_GOOGLE_KEY);
        return ok();
    }


    @ApiOperation(value = "校验验证码-关闭/开启Google验证器-APP")
    @PostMapping(value = "/checkCodeCloseOrOpenForApp")
    public ResultEntity checkCodeCloseOrOpenForApp(@RequestParam String uuid, @RequestParam Integer type, @RequestParam String code) throws Exception {
        if (StringUtils.isBlank(uuid)) {
            return fail("uuid不能为空");
        }
        if (StringUtils.isBlank(type)) {
            return fail("type不能为空");
        }
        if (StringUtils.isBlank(code)) {
            return fail("code不能为空");
        }
        Users users = usersService.selectByUuid(uuid);
        boolean b;
        //校验短信验证码
        if (type == Constants.MOBILE_TYPE) {
            b = registLoginService.checkSmsOrEmailCode(users.getMobile(), code, Constants.CLOSE_OR_OPEN_GOOGLE_AUTH_MOBILE);
        } else {
            b = registLoginService.checkSmsOrEmailCode(users.getEmail(), code, Constants.CLOSE_OR_OPEN_GOOGLE_AUTH_MOBILE);
        }
        if (!b && type == Constants.MOBILE_TYPE) {
            return fail("短信验证码校验失败");
        } else if (!b && type == Constants.EMAIL_TYPE) {
            return fail("邮箱验证码校验失败");
        }
        //生成一个authKey回传到前台，用于鉴定重设密码接口是否允许执行
        String s = RandomUtils.randomCustomUUID();
        if (type == Constants.MOBILE_TYPE) {
            redisService.put(CacheID.CLOSE_OR_OPEN_GOOGLE_KEY_AUTH_KEY + users.getMobile(), s, 600);
        } else {
            redisService.put(CacheID.CLOSE_OR_OPEN_GOOGLE_KEY_AUTH_KEY + users.getEmail(), s, 600);
        }
        return ok(s);
    }


    @ApiOperation(value = "校验原谷歌验证码-修改google验证码")
    @PostMapping(value = "/checkOldGoogleAuthKey")
    public ResultEntity checkOldGoogleAuthKey(@RequestParam String uuid,
                                              @RequestParam Integer type,
                                              @RequestParam String code,
                                              @RequestParam String googleCode) {
        if (StringUtils.isBlank(uuid)) {
            return fail("uuid不能为空");
        }
        if (StringUtils.isBlank(type)) {
            return fail("type不能为空");
        }
        if (StringUtils.isBlank(code)) {
            return fail("code不能为空");
        }
        if (StringUtils.isBlank(googleCode)) {
            return fail("googleCode不能为空");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("参数类型错误");
        }
        Users users = usersService.selectByUuid(uuid);
        boolean b;
        //校验短信验证码
        if (type == Constants.MOBILE_TYPE) {
            b = registLoginService.checkSmsOrEmailCode(users.getMobile(), code, Constants.MODIFY_GOOGLE_KEY_MOBILE);
        } else {
            b = registLoginService.checkSmsOrEmailCode(users.getEmail(), code, Constants.MODIFY_GOOGLE_KEY_EMAIL);
        }
        if (!b && type == Constants.MOBILE_TYPE) {
            return fail("短信验证码校验失败");
        } else if (!b) {
            return fail("邮箱验证码校验失败");
        }
        //解密
        String decrypKey = ToolAES.decrypt(users.getGoogleKey());
        //校验原谷歌验证码
        if (!GoogleAuthenticator.verify(googleCode, decrypKey)) {
            return fail("原谷歌授权码校验失败");
        }
        ;
        //生成一个authKey回传到前台，用于鉴定重设密码接口是否允许执行
        String s = RandomUtils.randomCustomUUID();
        redisService.put(CacheID.MODIFY_GOOGLE_KEY_AUTH_KEY + users.getUuid(), s, 600);
        return ok(s);
    }


    @ApiOperation(value = "修改google验证码")
    @PostMapping(value = "/modifyGoogleAuthKey")
    public ResultEntity modifyGoogleAuthKey(@RequestParam String uuid,
                                            @RequestParam String authKey,
                                            @RequestParam String googleCode) throws Exception {
        Users users = usersService.selectByUuid(uuid);
        //校验authKey
        String key = redisService.get(CacheID.MODIFY_GOOGLE_KEY_AUTH_KEY + users.getUuid());
        if (!authKey.equals(key)) {
            return fail("验证失败");
        }
        //获取google私钥
        String googleKey = redisService.get(CacheID.BIND_GOOGLE_KEY + uuid);
        if (StringUtils.isBlank(googleKey)) {
            return fail("google私钥不存在或已超时");
        }
        //校验Google授权码
        if (!GoogleAuthenticator.verify(googleCode, googleKey)) {
            return fail("Google授权码校验失败");
        }
        //googleKey加密
        String googleKeyAes = ToolAES.encrypt(googleKey);
        users.setGoogleKey(googleKeyAes);
        //2.更新用户信息
        usersService.updateUsers(new Users(u -> {
            u.setId(users.getId());
            u.setGoogleKey(googleKeyAes);
        }));
        //删除google私钥
        redisService.del(CacheID.BIND_GOOGLE_KEY + uuid);
        //删除autheKey
        redisService.del(CacheID.MODIFY_GOOGLE_KEY_AUTH_KEY + users.getUuid());
        sendMessageProducer.sendLetter(users.getId(), MsgTempNID.MODIFY_GOOGLE_KEY);
        return ok();


    }


    @ApiOperation(value = "开启/关闭谷歌验证器校验")
    @PostMapping(value = "/checkCloseOrOpenGoogleAuth")
    public ResultEntity checkCloseOrOpenGoogleAuth(@RequestParam String uuid, @RequestParam Integer state) {
        if (StringUtils.isBlank(uuid)) {
            return fail("uuid不能为空");
        }
        if (StringUtils.isBlank(state)) {
            return fail("state不能为空");
        }
        Users user = usersService.selectByUuid(uuid);
        //校验谷歌验证器状态
        if (state == Users.GOOGLESTATE.open.code && Users.GOOGLESTATE.open.code == user.getGoogleState()) {
            return fail("谷歌验证器已开启");
        } else if (state == Users.GOOGLESTATE.close.code && Users.GOOGLESTATE.close.code == user.getGoogleState()) {
            return fail("谷歌验证器已关闭");
        }
        //验证用户认证并返回用户的手机号码/邮箱
        UserIdentify userIdentify = userIdentifyService.selectOne(new UserIdentify(u -> {
            u.setUserId(user.getId());
            u.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        Map<String, Object> map = new HashMap<>();
        if (userIdentify.getMobileState() == UserIdentify.MOBILESTATE.CERTIFIED.code) {
            String hideUserName = hideUserName(user.getMobile(), Constants.MOBILE_TYPE);
            map.put("username", hideUserName);
            map.put("type", Constants.MOBILE_TYPE);
        } else if (userIdentify.getEmailState() == UserIdentify.EMAILSTATE.CERTIFIED.code) {
            String hideUserName = hideUserName(user.getEmail(), Constants.EMAIL_TYPE);
            map.put("username", hideUserName);
            map.put("type", Constants.EMAIL_TYPE);
        } else {
            return fail("用户认证异常");
        }
        return ok(map);
    }


    @ApiOperation(value = "关闭/开启Google验证码")
    @PostMapping(value = "/doCloseOrOpenGoogleAuth")
    public ResultEntity doCloseOrOpenGoogleAuth(@RequestParam String uuid,
                                                @RequestParam Integer type,
                                                @RequestParam Integer googleState,
                                                @RequestParam String code,
                                                @RequestParam String googleCode) throws Exception {
        if (StringUtils.isBlank(uuid)) {
            return fail("uuid不能为空");
        }
        if (StringUtils.isBlank(type)) {
            return fail("type不能为空");
        }
        if (StringUtils.isBlank(googleState)) {
            return fail("googleState不能为空");
        }
        if (StringUtils.isBlank(code)) {
            return fail("code不能为空");
        }
        if (StringUtils.isBlank(googleCode)) {
            return fail("googleCode不能为空");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("参数类型错误");
        }
        if (googleState != Users.GOOGLESTATE.close.code && googleState != Users.GOOGLESTATE.open.code) {
            return fail("参数类型错误");
        }
        Users users = usersService.selectByUuid(uuid);
        boolean b;
        //校验短信验证码
        if (type == Constants.MOBILE_TYPE) {
            b = registLoginService.checkSmsOrEmailCode(users.getMobile(), code, Constants.CLOSE_OR_OPEN_GOOGLE_AUTH_MOBILE);
        } else {
            b = registLoginService.checkSmsOrEmailCode(users.getEmail(), code, Constants.CLOSE_OR_OPEN_GOOGLE_AUTH_EMAIL);
        }
        if (!b && type == Constants.MOBILE_TYPE) {
            return fail("短信验证码校验失败");
        } else if (!b) {
            return fail("邮箱验证码校验失败");
        }
        String googleKey = ToolAES.decrypt(users.getGoogleKey());
        //校验Google授权码
        if (!GoogleAuthenticator.verify(googleCode, googleKey)) {
            return fail("Google授权码校验失败");
        }
        //2.更新用户信息
        usersService.updateUsers(new Users(u -> {
            u.setId(users.getId());
            u.setGoogleState(googleState);
        }));
        if (googleState == Users.GOOGLESTATE.close.code) {
            sendMessageProducer.sendLetter(users.getId(), MsgTempNID.CLOSE_GOOGLE_KEY);
        } else {
            sendMessageProducer.sendLetter(users.getId(), MsgTempNID.OPEN_GOOGLE_KEY);
        }
        return ok();
    }


    @ApiOperation(value = "关闭/开启Google验证码-APP")
    @PostMapping(value = "/doCloseOrOpenGoogleAuthForApp")
    public ResultEntity doCloseOrOpenGoogleAuthForApp(@RequestParam String uuid,
                                                      @RequestParam Integer type,
                                                      @RequestParam Integer googleState,
                                                      @RequestParam String authKey,
                                                      @RequestParam String googleCode) throws Exception {
        if (StringUtils.isBlank(uuid)) {
            return fail("uuid不能为空");
        }
        if (StringUtils.isBlank(type)) {
            return fail("type不能为空");
        }
        if (StringUtils.isBlank(googleState)) {
            return fail("googleState不能为空");
        }
        if (StringUtils.isBlank(authKey)) {
            return fail("authKey不能为空");
        }
        if (StringUtils.isBlank(googleCode)) {
            return fail("googleCode不能为空");
        }
        if (type != Constants.MOBILE_TYPE && type != Constants.EMAIL_TYPE) {
            return fail("参数类型错误");
        }
        if (googleState != Users.GOOGLESTATE.close.code && googleState != Users.GOOGLESTATE.open.code) {
            return fail("参数类型错误");
        }
        Users users = usersService.selectByUuid(uuid);
        String key;
        if (type == Constants.MOBILE_TYPE) {
            key = redisService.get(CacheID.CLOSE_OR_OPEN_GOOGLE_KEY_AUTH_KEY + users.getMobile());
        } else {
            key = redisService.get(CacheID.CLOSE_OR_OPEN_GOOGLE_KEY_AUTH_KEY + users.getEmail());
        }
        if (!key.equals(authKey)) {
            return fail("验证校验失败");
        }
        String googleKey = ToolAES.decrypt(users.getGoogleKey());
        //校验Google授权码
        if (!GoogleAuthenticator.verify(googleCode, googleKey)) {
            return fail("Google授权码校验失败");
        }
        //2.更新用户信息
        usersService.updateUsers(new Users(u -> {
            u.setId(users.getId());
            u.setGoogleState(googleState);
        }));
        //删除验证
        if (type == Constants.MOBILE_TYPE) {
            redisService.del(CacheID.CLOSE_OR_OPEN_GOOGLE_KEY_AUTH_KEY + users.getMobile());
        } else {
            redisService.del(CacheID.CLOSE_OR_OPEN_GOOGLE_KEY_AUTH_KEY + users.getEmail());
        }
        if (googleState == Users.GOOGLESTATE.close.code) {
            sendMessageProducer.sendLetter(users.getId(), MsgTempNID.CLOSE_GOOGLE_KEY);
        } else {
            sendMessageProducer.sendLetter(users.getId(), MsgTempNID.OPEN_GOOGLE_KEY);
        }
        return ok();
    }
}
