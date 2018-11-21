/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.user.controller;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ToolAES;
import com.mi.hundsun.oxchains.base.core.auth.google.GoogleAuthenticator;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.constant.CoinCode;
import com.mi.hundsun.oxchains.base.core.constant.Constants;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.PlatUserAddress;
import com.mi.hundsun.oxchains.base.core.po.tpl.Protocol;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.base.core.po.tpl.TradeFee;
import com.mi.hundsun.oxchains.base.core.po.user.UserFreeze;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import com.mi.hundsun.oxchains.base.core.po.user.UserInLetter;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.base.core.service.fn.PlatUserAddressService;
import com.mi.hundsun.oxchains.base.core.service.tpl.ProtocolService;
import com.mi.hundsun.oxchains.base.core.service.tpl.ServiceFeeService;
import com.mi.hundsun.oxchains.base.core.service.tpl.TradeFeeService;
import com.mi.hundsun.oxchains.base.core.service.user.*;
import com.mi.hundsun.oxchains.provider.user.rabbitmq.SysSmsMessageProducer;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 枫亭
 * @date 2018-04-08 20:58.
 */
@Slf4j
@Api(value = "用户模块相关服务", description = "RegistLoginController Created By 枫亭 at 2018-04-08 20:58")
@RestController
@RequestMapping("/prod/user")
public class UserController extends GenericController {

    @Autowired
    UsersService usersService;
    @Autowired
    UserInLetterService userInLetterService;
    @Autowired
    RegistLoginService registLoginService;
    @Autowired
    UserRiskControlService userRiskControlService;
    @Autowired
    RedisService redisService;
    @Autowired
    ServiceFeeService serviceFeeService;
    @Autowired
    UserIdentifyService userIdentifyService;
    @Autowired
    UserFreezeService userFreezeService;
    @Autowired
    TradeFeeService tradeFeeService;
    @Autowired
    ProtocolService protocolService;
    @Autowired
    PlatUserAddressService platUserAddressService;
    @Autowired
    SysSmsMessageProducer sendMessageProducer;

    @PostMapping("/selectByUuid")
    public Users selectByUuid(@RequestParam String uuid) {
        if (StringUtils.isBlank(uuid)) {
            throw new BussinessException("参数不能为空");
        }
        return usersService.selectByUuid(uuid);
    }

    @PostMapping("/selectByEmail")
    public Users selectByEmail(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            throw new BussinessException("参数不能为空");
        }
        return usersService.selectByEmail(username);
    }

    @PostMapping("/selectByMobile")
    public Users selectByMobile(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            throw new BussinessException("参数不能为空");
        }
        return usersService.selectByMobile(username);
    }

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insert(@RequestBody Users users) throws BussinessException {
        usersService.insert(users);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Users users) throws BussinessException {
        usersService.updateByPrimaryKeySelective(users);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/getNormalModelById")
    public Users getNormalModelById(@RequestBody Users users) throws BussinessException {
        return usersService.getNormalModelById(users);
    }

    @ApiOperation(value = "校验验证码")
    @PostMapping(value = "/checkCodeForGoogleAuth")
    public ResultEntity checkCodeForGoogleAuth(@RequestParam String mobile,
                                               @RequestParam String code,
                                               @RequestParam String type) {
        //判断图文验证码是否正确
        boolean b = registLoginService.checkSmsOrEmailCode(mobile, code, type);
        if (!b) {
            throw new BussinessException("邮箱验证码错误");
        }
        return ok();
    }

    @ApiOperation(value = "生成Google私钥")
    @PostMapping(value = "/generateGoogleAuthKey")
    public ResultEntity generateGoogleAuthKey(@RequestParam String uuid) throws Exception {
        if (StrUtil.isBlank(uuid)) {
            throw new BussinessException("uuid不能为空");
        }
        Users user = usersService.selectByUuid(uuid);
        String googleKey = user.getGoogleKey();
        if (StrUtil.isBlank(googleKey)) {
            //1.生成秘钥保存到用户信息
            googleKey = GoogleAuthenticator.generateSecretKey();
            if (null == googleKey) {
                throw new BussinessException("生成GoogleKey失败");
            }
            //googleKey加密
            String googleKeyAes = ToolAES.encrypt(googleKey);
            user.setGoogleKey(googleKeyAes);
            //2.更新用户信息
            usersService.updateUsers(user);
        }
        //2.生成二维码地址
        String username;
        if (StrUtil.isBlank(user.getEmail())) {
            username = user.getMobile();
        } else {
            username = user.getEmail();
        }
        String qrBarcode = GoogleAuthenticator.getQRBarcode(username, googleKey);
        //TODO 调用云服务文件存储把生成的二维码地址生成图片并上传到云端。
        Map<String, Object> map = new HashMap<>();
        map.put("key", googleKey);
        map.put("qrCodePath", qrBarcode);
        return ok(map);
    }

    /**
     * 隐藏用户名
     *
     * @param userName 用户名
     * @param type     1，手机号，2，邮箱
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


    @ApiOperation(value = "获取我的消息", notes = "查询用户的消息-包含系统发送的公告信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户的id", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
    })
    @PostMapping(value = "/getMyMsg")
    public ResultEntity getMyMsg(@RequestParam("userId") Integer userId, @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam("pageNumber") Integer pageNumber) {
        if (null == userId || 0 == userId) {
            throw new BussinessException("userId不能为空");
        }
        if (null == pageSize) {
            throw new BussinessException("pageSize不能为空");
        }
        if (null == pageNumber) {
            throw new BussinessException("pageNumber不能为空");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            long recordCount = userInLetterService.userInLetterCount(userId);
            //总分页数
            int pageNum = (int) recordCount / pageSize + (recordCount % pageSize > 0 ? 1 : 0);
            List<UserInLetter> list = userInLetterService.userInLetterList(userId, pageSize, pageNumber);
            map.put("recordCount", recordCount);
            map.put("pageNum", pageNum);
            map.put("data", list);
            return ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    @ApiOperation(value = "清空我的消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户的id", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "ids", value = "idList", required = true, dataType = "List<Integer>")
    })
    @PostMapping(value = "/clearMyMsg")
    public ResultEntity clearMyMsg(@RequestParam("userId") Integer userId, @RequestParam("ids") List<Integer> ids) {
        if (null == userId || 0 == userId) {
            throw new BussinessException("userId不能为空");
        }
        try {
            boolean b = userInLetterService.clearMyMsg(userId, ids);
            if (b) {
                return ok();
            }
            return fail("清空我的消息失败");
        } catch (Exception e) {
            e.printStackTrace();
            return fail("清空我的消息失败");
        }
    }


    @ApiOperation(value = "境内用户实名认证")
    @PostMapping(value = "/idCardIdentify")
    public ResultEntity idCardIdentify(@RequestParam("userId") Integer userId,
                                       @RequestParam("realname") String realname,
                                       @RequestParam("idCardNo") String idCardNo,
                                       @RequestParam("idCardFrontPic") String idCardFrontPic,
                                       @RequestParam("idCardReversePic") String idCardReversePic) {
        if (null == userId || 0 == userId) {
            return fail("[userId]不能为空");
        }
        if (StrUtil.isBlank(realname)) {
            return fail("[realname]不能为空");
        }
        if (StrUtil.isBlank(idCardNo)) {
            return fail("[idCardNo]不能为空");
        }
        if (StrUtil.isBlank(idCardFrontPic)) {
            return fail("[idCardFrontPic]不能为空");
        }
        if (StrUtil.isBlank(idCardReversePic)) {
            return fail("[idCardReversePic]不能为空");
        }

        UserIdentify userIdentify = userIdentifyService.selectOne(new UserIdentify(u -> {
            u.setUserId(userId);
            u.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if (userIdentify.getRealnameState() == UserIdentify.REALNAMESTATE.CERTIFIED.code) {
            return fail("已实名认证成功!");
        }
        if (userIdentify.getRealnameState() == UserIdentify.REALNAMESTATE.WAITING.code) {
            return fail("已提交认证，请等待审核");
        }
        //保存用户信息
        try {
            Users user = new Users();
            user.setId(userId);
            user.setRealname(realname);
            user.setIdNo(idCardNo);
            user.setIdType(Users.IDTYPE.IDCARD.code);
            user.setCountry(Users.COUNTRY.DOMESTIC.code);
            user.setUpdateTime(new Date());
            usersService.idCardIdentify(user, idCardFrontPic, idCardReversePic, userIdentify.getId());
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail("实名认证失败！");
        }
    }

    @ApiOperation(value = "校验用户提现密码")
    @PostMapping(value = "/checkMentionPwd")
    public Boolean checkMentionPwd(@RequestParam("userId") Integer userId,
                                   @RequestParam("encryptMentionPwd") String encryptMentionPwd) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (null == userId || 0 == userId) {
            throw new BussinessException("[userId]不能为空");
        }
        if (StrUtil.isBlank(encryptMentionPwd)) {
            throw new BussinessException("[encryptMentionPwd]不能为空");
        }
        return usersService.checkMentionPwd(userId, encryptMentionPwd);
    }

    @ApiOperation(value = "校验用户交易信息", notes = "如用户实名状态、交易状态、是否达到风控值等")
    @PostMapping(value = "/preValidUserInfoToTx")
    public ResultEntity preValidUserInfoToTx(@RequestParam("userId") Integer userId,
                                             @RequestParam("netWorth") BigDecimal netWorth) {
        if (null == userId || 0 == userId) {
            throw new BussinessException("[userId]不能为空");
        }
        if (null == netWorth) {
            throw new BussinessException("[netWorth]不能为空");
        }
        boolean b = usersService.preValidUserInfoToTx(userId, netWorth);
        return b ? ok() : fail();
    }

    @ApiOperation(value = "获取用户交易手续费模板", notes = "获取用户交易手续费模板")
    @PostMapping(value = "/getTradeFeeTpl")
    public ResultEntity getTradeFeeTpl(@RequestParam("currencyPair") String currencyPair) {
        if (StrUtil.isNotBlank(currencyPair) && !currencyPair.contains("_")) {
            throw new BussinessException("[currencyPair]格式不合法");
        }
        TradeFee fee = new TradeFee();
        fee.setSymbol(currencyPair);
        fee.setState(TradeFee.STATE.ENABLE.code);

        TradeFee tradeFee = tradeFeeService.selectOne(fee);
        if (null != tradeFee) {
            return ok(JSON.toJSONString(tradeFee));
        }
        return fail("未查询到交易手续费模板");
    }

    @ApiOperation(value = "获取提币手续费模板（仅用于提币手续费)", notes = "获取提币手续费模板（仅用于提币手续费)")
    @PostMapping(value = "/findServiceFeeTplByUserId")
    public ResultEntity findServiceFeeTplByUserId(@RequestParam("userId") Integer userId, @RequestParam("code") String code) {
        if (null == userId || 0 == userId) {
            throw new BussinessException("[userId]不能为空");
        }
        List<ServiceFee> tplFees = serviceFeeService.select(new ServiceFee(f -> {
            f.setCoinCurrency(code);
            f.setDelFlag(GenericPo.DELFLAG.NO.code);
            f.setState(ServiceFee.STATE.ENABLE.code);
        }));
        ServiceFee tplFee ;
        if(null != tplFees && tplFees.size() > 0) {
            tplFee = tplFees.get(0);
        } else {
            tplFee = new ServiceFee();
            tplFee.setServiceFee(new BigDecimal(0));
            tplFee.setOnceMinAmount(new BigDecimal(0));
            tplFee.setTodayMaxAmount(new BigDecimal(0));
        }

        Map<String, Object> map = new HashMap<>();
        if (null != tplFee) {
            map.put("onceMinAmount", tplFee.getOnceMinAmount());
            map.put("serviceFee", tplFee.getServiceFee());
            map.put("todayMaxAmount", tplFee.getTodayMaxAmount());
            map.put("tplNo", tplFee.getTplNo());
        }
        return ok(map);
    }


    @ApiOperation(value = "境外用户实名认证")
    @PostMapping(value = "/passportIdentify")
    public ResultEntity passportIdentify(@RequestParam("userId") Integer userId,
                                         @RequestParam("realname") String realname,
                                         @RequestParam("passportNo") String passportNo,
                                         @RequestParam("passportPic") String passportPic) {


        UserIdentify userIdentify = userIdentifyService.selectOne(new UserIdentify(u -> {
            u.setUserId(userId);
            u.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if (userIdentify.getRealnameState() == UserIdentify.REALNAMESTATE.CERTIFIED.code) {
            return fail("已实名认证成功!");
        }
        if (userIdentify.getRealnameState() == UserIdentify.REALNAMESTATE.WAITING.code) {
            return fail("已提交认证，请等待审核");
        }
        try {
            Users u = new Users();
            u.setId(userId);
            u.setRealname(realname);
            u.setIdNo(passportNo);
            u.setType(Users.IDTYPE.PASSPORT.code);
            u.setCountry(Users.COUNTRY.ABROAD.code);
            u.setUpdateTime(new Date());
            usersService.passportIdentify(u, passportPic, userIdentify.getId());
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail("提交认证失败");
        }
    }

    @ApiOperation(value = "校验用提币冻结")
    @PostMapping(value = "/checkMentionCoinState")
    public boolean checkMentionCoinState(@RequestParam("userId") Integer userId) {
        //提币是否冻结
        UserFreeze userFreeze = userFreezeService.selectOne(new UserFreeze(u -> {
            u.setUserId(userId);
            u.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        return userFreeze.getMentionCoinState() != UserFreeze.MENTIONCOINSTATE.FROZEN.code;
    }

    @ApiOperation(value = "校验是否已手机认证")
    @PostMapping(value = "/checkMobileAuth")
    public ResultEntity checkMobileAuth(@RequestParam("userId") Integer userId, @RequestParam("mobile") String mobile) {
        Users users = usersService.selectOne(new Users(u -> {
            u.setId(userId);
            u.setMobile(mobile);
            u.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if (null == users) {
            return fail("注册手机号码不正确");
        }
        UserIdentify userIdentify = userIdentifyService.selectOne(new UserIdentify(u -> {
            u.setUserId(userId);
            u.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if (userIdentify.getMobileState() != UserIdentify.MOBILESTATE.CERTIFIED.code) {
            return fail("手机号码未绑定");
        }
        return ok();
    }


    @ApiOperation(value = "校验是否已邮箱认证")
    @PostMapping(value = "/checkEmailAuth")
    public ResultEntity checkEmailAuth(@RequestParam("userId") Integer userId, @RequestParam("email") String email) {
        Users users = usersService.selectOne(new Users(u -> {
            u.setId(userId);
            u.setEmail(email);
            u.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if (null == users) {
            return fail("注册邮箱不正确");
        }
        UserIdentify userIdentify = userIdentifyService.selectOne(new UserIdentify(u -> {
            u.setUserId(userId);
            u.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if (userIdentify.getEmailState() != UserIdentify.EMAILSTATE.CERTIFIED.code) {
            return fail("邮箱未绑定");
        }
        return ok();
    }

    @ApiOperation(value = "查询风险提示协议")
    @PostMapping(value = "/getRiskProtocol")
    public ResultEntity getRiskProtocol() {
        Protocol p = new Protocol();
        p.setId(2);
        p.setDelFlag(Protocol.DELFLAG.NO.code);
        Protocol protocol = protocolService.selectOne(p);
        return null == protocol ? fail("未查询到具体协议") : ok(protocol);
    }

    @ApiOperation(value = "查询注册协议")
    @PostMapping(value = "/getRegistProtocol")
    public ResultEntity getRegistProtocol() {
        Protocol p = new Protocol();
        p.setId(1);
        p.setDelFlag(Protocol.DELFLAG.NO.code);
        Protocol protocol = protocolService.selectOne(p);
        return null == protocol ? fail("未查询到具体协议") : ok(protocol);
    }


    @ApiOperation(value = "分配用户地址")
    @PostMapping(value = "/distributeAddressToUser")
    public void distributeAddressToUser(@RequestParam Integer userId) {
        try {
            //查询该userId是否已经分配了地址
            PlatUserAddress address = new PlatUserAddress();
            address.setUserId(userId);
            address.setDelFlag(PlatUserAddress.DELFLAG.NO.code);
            address.setCoinCurrency(CoinCode.BTC);
            List<PlatUserAddress> selectBtc = platUserAddressService.select(address);
            int btc = 0;
            if(null != selectBtc && selectBtc.size() > 1) {
                btc = 1;
            }
            address.setCoinCurrency(CoinCode.ETH);
            List<PlatUserAddress> selectEth = platUserAddressService.select(address);
            int eth = 0;
            if(null != selectEth && selectEth.size() > 1) {
                eth = 1;
            }
            List<PlatUserAddress> noDistributedAddress = platUserAddressService.findNoDistributedAddress();
            for (PlatUserAddress userAddress : noDistributedAddress) {
                if(userAddress.getCoinCurrency().equals(CoinCode.BTC) && btc == 0) {
                    userAddress.setUserId(userId);
                    userAddress.setState(PlatUserAddress.STATE.DISTRIBUTED.code);
                    userAddress.setUpdateTime(new Date());
                    platUserAddressService.updateByPrimaryKeySelective(userAddress);
                } else if(userAddress.getCoinCurrency().equals(CoinCode.ETH) && eth == 0) {
                    userAddress.setUserId(userId);
                    userAddress.setState(PlatUserAddress.STATE.DISTRIBUTED.code);
                    userAddress.setUpdateTime(new Date());
                    platUserAddressService.updateByPrimaryKeySelective(userAddress);
                }
            }
        } catch (Exception e) {
            log.error("分配用户充币地址出错,{}", e.getMessage());
        }
    }

}
