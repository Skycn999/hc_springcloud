/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.controller.user;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ToolAES;
import com.mi.hundsun.oxchains.base.core.auth.google.GoogleAuthenticator;
import com.mi.hundsun.oxchains.base.core.common.CommodityModel;
import com.mi.hundsun.oxchains.base.core.constant.CoinCode;
import com.mi.hundsun.oxchains.base.core.constant.Constants;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import com.mi.hundsun.oxchains.base.core.util.TxUtils;
import com.mi.hundsun.oxchains.consumer.web.config.WebGenericController;
import com.mi.hundsun.oxchains.consumer.web.service.tx.AccountInterface;
import com.mi.hundsun.oxchains.consumer.web.service.tx.QuoteHuoBiInterface;
import com.mi.hundsun.oxchains.consumer.web.service.user.*;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;


/**
 * @author 枫亭
 * @description 账户中心
 * @date 2018-04-13 19:46.
 */
@RestController
@RequestMapping("/api/web/account/central")
public class AccountCentralController extends WebGenericController {
    @Autowired
    PlatUserAddressInterface platUserAddressInterface;
    @Autowired
    RechargeCoinInterface rechargeCoinInterface;
    @Autowired
    AccountInterface accountInterface;
    @Autowired
    AccountCentralInterface accountCentralInterface;
    @Autowired
    UserInterface userInterface;
    @Autowired
    RegistLoginInterface registLoginInterface;
    @Autowired
    private QuoteHuoBiInterface huoBiInterface;
//    @Autowired
//    SysSmsMessageProducer sendMessageProducer;


    @ApiOperation(value = "我的资产列表", notes = "获取我的资产持仓列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
    })
    @RequestMapping("/myAccountList")
    public ResultEntity myAccountList() {
        ResultEntity resultEntity = accountInterface.selectByUserId(getLoginUserId());
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            List<Account> accounts = JSON.parseArray(resultEntity.getData().toString(), Account.class);
            BigDecimal netWorth = computeNetWorth(accounts);
            Map<String, Object> map = new HashMap<>();
            map.put("accounts", accounts);
            map.put("netWorth", netWorth);
            return ok(map);
        }
        return fail("获取可用资产失败");
    }

    public BigDecimal computeNetWorth() {
        ResultEntity resultEntity = accountInterface.selectByUserId(getLoginUserId());
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            List<Account> accounts = JSON.parseArray(resultEntity.getData().toString(), Account.class);
            BigDecimal netWorth = this.computeNetWorth(accounts);
            return new BigDecimal(netWorth.stripTrailingZeros().toPlainString());
        }
        return BigDecimal.ZERO;
    }

    /**
     * 计算账户当前净资产
     *
     * @return 净资产
     */
    @PostMapping(value = "/getNetWorth")
    public ResultEntity getNetWorth() {
        ResultEntity resultEntity = accountInterface.selectByUserId(getLoginUserId());
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            List<Account> accounts = JSON.parseArray(resultEntity.getData().toString(), Account.class);
            BigDecimal netWorth = this.computeNetWorth(accounts);
            return ok(netWorth);
        }
        return ok(BigDecimal.ZERO);
    }

    @ApiOperation(value = "我的认证信息", notes = "获取我的认证信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
    })
    @RequestMapping("/myIdentifyInfo")
    public ResultEntity myIdentifyInfo(@RequestParam String uuid) {
        Users user = userInterface.selectByUuid(uuid);
        if (null == user) {
            return fail("No This User！");
        }
        String userName;
        if (!StringUtils.isBlank(user.getMobile())) {
            userName = hideUserName(user.getMobile(), Constants.MOBILE_TYPE);
        } else if (!StringUtils.isBlank(user.getEmail())) {
            userName = hideUserName(user.getEmail(), Constants.EMAIL_TYPE);
        } else {
            return fail("用户信息有误！");
        }
        Map<String, Object> map = new HashMap<>();
        ResultEntity resultEntity = accountCentralInterface.getUserIdentifyInfo(getLoginUserId());
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            map.put("userName", userName);
            map.put("userIdentify", resultEntity.getData());
            return ok(map);
        }
        return fail();
    }

    /**
     * 隐藏用户名
     *
     * @param userName 待处理用户名
     * @param type     1，手机号，2，邮箱
     * @return 隐藏后的用户名
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


    @ApiOperation(value = "提币校验", notes = "用户提币校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
    })
    @RequestMapping("/checkMyMentionCoin")
    public ResultEntity checkMyMentionCoin() {
        ResultEntity resultEntity = accountCentralInterface.checkMyMentionCoin(getLoginUserId());
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        }
        return resultEntity;
    }

    @ApiOperation(value = "校验提币密码", notes = "校验提币密码(同时返回安全验证手机号或/邮箱及谷歌验证器状态)-用于是否弹出安全验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptMentionPwd", value = "加密后的提币密码", required = true, dataType = "String")
    })
    @RequestMapping("/checkMentionPwdForSafe")
    public ResultEntity checkMentionPwdForSafe(@RequestParam String encryptMentionPwd) {
        return accountCentralInterface.checkMentionPwdForSafe(getLoginUserId(), encryptMentionPwd);
    }


    @ApiOperation(value = "获取验证码-安全验证", notes = "获取验证码-安全验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "type", value = "发送验证码的类型1，手机号 2，邮箱", required = true, dataType = "Integer")
    })
    @RequestMapping("/getCodeForSafe")
    public ResultEntity getCodeForSafe(@RequestParam String uuid, @RequestParam Integer type) {
        if (null == uuid || null == type) {
            return fail("参数错误");
        }
        Users users = userInterface.selectByUuid(uuid);

        //发送验证码
        if (type == Constants.EMAIL_TYPE) {
            registLoginInterface.sendEmailOrSms(users.getEmail(), Constants.MENTION_COIN_CODE_EMAIL);
        } else {
            registLoginInterface.sendEmailOrSms(users.getMobile(), Constants.MENTION_COIN_CODE_MOBILE);
        }
        return ok();
    }

    @ApiOperation(value = "校验安全验证", notes = "校验安全验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "type", value = "发送验证码的类型1，手机号 2，邮箱", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "code", value = "短信或邮箱验证码", required = true, dataType = "String")
            , @ApiImplicitParam(name = "googleCode", value = "谷歌验证码", dataType = "String")
    })
    @RequestMapping("/checkGoogleCode")
    public ResultEntity checkGoogleCode(@RequestParam String uuid, @RequestParam Integer type, @RequestParam String code, @RequestParam String googleCode) {
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(type) || StringUtils.isBlank(code)) {
            return fail("参数错误");
        }
        //校验验证码
        Users users = userInterface.selectByUuid(uuid);
        boolean b = users.getGoogleState() == Users.GOOGLESTATE.open.code;
        if (b && StringUtils.isBlank(googleCode)) {
            return fail("谷歌验证码为空");
        }
        ResultEntity resultEntity;
        if (type == Constants.EMAIL_TYPE) {
            resultEntity = registLoginInterface.checkCodeForRegist(users.getEmail(), code, Constants.MENTION_COIN_CODE_EMAIL);
        } else {
            resultEntity = registLoginInterface.checkCodeForRegist(users.getMobile(), code, Constants.MENTION_COIN_CODE_MOBILE);
        }
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            if (type == Constants.EMAIL_TYPE) {
                return fail("邮箱验证码错误");
            } else {
                return fail("短信验证码错误");
            }
        }
        if (b) {
            //googleCode
            String googleKey = ToolAES.decrypt(users.getGoogleKey());
            boolean b1 = GoogleAuthenticator.verify(googleCode, googleKey);
            if (!b1) {
                return fail("谷歌验证码错误");
            }
        }
        return ok();
    }


    @ApiOperation(value = "提币操作", notes = "用户提币操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "code", value = "提币币种", required = true, dataType = "String")
            , @ApiImplicitParam(name = "address", value = "提币地址", required = true, dataType = "String")
            , @ApiImplicitParam(name = "amount", value = "提币数量", required = true, dataType = "String")
            , @ApiImplicitParam(name = "serviceFee", value = "手续费", required = true, dataType = "String")
            , @ApiImplicitParam(name = "encryptMentionPwd", value = "加密后的提现密码", required = true, dataType = "String")
    })
    @RequestMapping("/doMentionCoin")
    public ResultEntity doMentionCoin(@RequestParam String code,
                                      @RequestParam String address,
                                      @RequestParam String amount,
                                      @RequestParam String serviceFee,
                                      @RequestParam String encryptMentionPwd) {
        if (StrUtil.isBlank(code)) {
            throw new BussinessException("[提币币种]不能为空");
        }
        if (!(code.equals(CoinCode.BTC) || code.equals(CoinCode.ETH))) {
            throw new BussinessException("[提币币种]参数错误");
        }
        if (StrUtil.isBlank(address)) {
            throw new BussinessException("[提币地址]不能为空");
        }
        if (StrUtil.isBlank(amount)) {
            throw new BussinessException("[提币数量]不能为空");
        }
        if (BigDecimal.valueOf(0).compareTo(new BigDecimal(amount)) >= 0) {
            throw new BussinessException("[提币数量]不能为零");
        }
        if (StrUtil.isBlank(serviceFee)) {
            throw new BussinessException("[手续费]不能为空");
        }
        if (StrUtil.isBlank(encryptMentionPwd)) {
            throw new BussinessException("[提币密码]不能为空");
        }
        Integer userId = getLoginUserId();
        //0.校验提现密码是否正确
        boolean b0 = accountCentralInterface.checkMentionPwd(userId, encryptMentionPwd);
        if (!b0) {
            return fail("提币密码不正确");
        }
        //1.校验手续费是否小于给用户设置提现手续费金额(防篡改)
        boolean b1 = accountCentralInterface.checkUserServiceFeeTpl(code, serviceFee);
        if (!b1) {
            return fail("手续费不正确");
        }
        //2.校验用户输入的提币地址是否是用户自己填写的。
        boolean b2 = accountCentralInterface.checkAddress(userId, address);
        if (!b2) {
            return fail("提币地址错误");
        }
        //3.校验用户输入的提币数量是否小于用户的可用数量。
        boolean b3 = accountInterface.checkAvailAmount(userId, code, amount, serviceFee);
        if (!b3) {
            return fail("提币数量大于可用数量");
        }
        //4.校验用提币冻结
        boolean b4 = userInterface.checkMentionCoinState(userId);
        if (!b4) {
            return fail("提币功能已冻结");
        }
        //校验单次提币最小限额和是否超出当日累计最大限额
        ResultEntity result = accountCentralInterface.checkMaxAmount(userId, code, amount);
        if (result.getCode() != ResultEntity.SUCCESS) {
            return result;
        }
        //新增提币记录
        ResultEntity resultEntity = accountCentralInterface.addMentionCoin(userId, address, code, amount, serviceFee);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            return fail("提币失败");
        }
        //执行提币操作(冻结资产、新增资产变更流水)
        ResultEntity resultEntity2 = accountInterface.doMentionCoin(userId, code, amount, serviceFee);
        if (resultEntity2.getCode() != ResultEntity.SUCCESS) {
            return fail("提币失败");
        }
        return ok();
    }

    @ApiOperation(value = "提币记录", notes = "我的提币记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
    })
    @RequestMapping("/myMentionCoinRecord")
    public ResultEntity myMentionCoinRecord(@RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        ResultEntity resultEntity = accountCentralInterface.myMentionCoinLog(getLoginUserId(), pageSize, pageNumber);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        }
        return fail();
    }

    @ApiOperation(value = "提币币种", notes = "获取我可以提币的币种(可用数量和冻结数量、提币地址列表、提币手续费模版)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "isMerge", value = "是否合并显示币种资产0否 1是", required = true, dataType = "Integer")
    })
    @RequestMapping("/getAvailMentionCurrency")
    public ResultEntity getAvailMentionCurrency(@RequestParam Integer isMerge) {
        ResultEntity resultEntity = accountInterface.getAvailMentionCurrency(getLoginUserId(), isMerge);
        //提币币种-对应可提币数量、冻结数据 和手续费
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            List<Account> accounts = JSON.parseArray(resultEntity.getData().toString(), Account.class);
            Map<String, Object> map = new LinkedHashMap<>();
            for (Account a : accounts) {
                Map<String, Object> codeMap = new LinkedHashMap<>();
                String coinCode = a.getCoinCode();
                codeMap.put("available", a.getAvailable());
                codeMap.put("freeze", a.getFreeze());
                codeMap.put("total", a.getTotal());

                ResultEntity feeTplByUserId = userInterface.findServiceFeeTplByUserId(getLoginUserId(), coinCode);
                if (feeTplByUserId.getCode() == ResultEntity.SUCCESS) {
                    codeMap.put("feeTpl", feeTplByUserId.getData());
                }
                ResultEntity result = accountCentralInterface.findMyMcAddressByCode(getLoginUserId(), coinCode);
                if (result.getCode() == ResultEntity.SUCCESS) {
                    codeMap.put("addressList", result.getData());
                }
                map.put(coinCode, codeMap);
            }
            return ok(map);
        }
        return fail(resultEntity.getMessage());
    }

    @ApiOperation(value = "充币地址", notes = "获取平台分配给用户的充币地址和地址二维码图片路径")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户uuid", required = true, dataType = "String"),
            @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "充币币种", required = true, dataType = "String")
    })
    @ApiResponse(code = 200, message = "正常返回address和qrPath")
    @RequestMapping("/myRechargeAddress")
    public ResultEntity myRechargeAddress(@RequestParam String code) {
        ResultEntity byUserId = platUserAddressInterface.getByUserIdAndCode(getLoginUserId(), code);
        if (byUserId.getCode() == ResultEntity.SUCCESS) {

            return ok(byUserId.getData());
        }
        return fail(byUserId.getMessage());
    }


    @ApiOperation(value = "充币记录", notes = "我的充币记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "当前登录用户id", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
    })
    @RequestMapping("/myRechargeRecord")
    public ResultEntity myRechargeRecord(@RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        ResultEntity entity = rechargeCoinInterface.getByUserId(getLoginUserId(), pageSize, pageNumber);
        if (entity.getCode() == ResultEntity.SUCCESS) {
            return ok(entity.getData());
        }
        return fail();
    }

    private BigDecimal computeNetWorth(List<Account> accounts) {
        if (null == accounts || accounts.size() < 1) {
            return BigDecimal.ZERO;
        }
        BigDecimal netWorth = BigDecimal.ZERO;
        //记录不能直接转换成BTC的币种
        List<Account> unableDirectConvertBtcAccounts = new ArrayList<>();
        //以BTC为分母进行转换
        for (Account account : accounts) {
            //有uSDT持仓 单独计算
            if (account.getCoinCode().equalsIgnoreCase(CoinCode.USDT)) {
                String symbol = CoinCode.BTC + "_" + CoinCode.USDT;
                ResultEntity jsonObject = JSON.parseObject(huoBiInterface.price(symbol), ResultEntity.class);
                List<CommodityModel> commodityModels = JSON.parseArray(jsonObject.getData().toString(), CommodityModel.class);
                if (null != commodityModels && commodityModels.size() > 0) {
                    CommodityModel model = commodityModels.get(0);
                    String lastPrice = model.getLastPrice();
                    String plainString = account.getTotal().
                            divide(new BigDecimal(lastPrice), 10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
                            .toPlainString();
                    netWorth = netWorth.add(new BigDecimal(plainString));
                }
                continue;
            }
            if (!account.getCoinCode().equalsIgnoreCase(CoinCode.BTC)) {
                String symbol = account.getCoinCode() + "_" + CoinCode.BTC;
                ResultEntity jsonObject = JSON.parseObject(huoBiInterface.price(symbol), ResultEntity.class);
                List<CommodityModel> commodityModels = JSON.parseArray(jsonObject.getData().toString(), CommodityModel.class);
                if (null != commodityModels && commodityModels.size() > 0) {
                    CommodityModel model = commodityModels.get(0);
                    String lastPrice = model.getLastPrice();
                    if (StrUtil.isNotBlank(lastPrice)) {
                        netWorth = netWorth.add(new BigDecimal(model.getLastPrice()).multiply(account.getTotal(), MathContext.DECIMAL32));
                    } else {
                        unableDirectConvertBtcAccounts.add(account);
                    }
                }
            } else {
                //资产列表中是否包含btc 包含的话 净资产直接加
                netWorth = netWorth.add(account.getTotal());
            }
        }

        //记录不能直接转换成ETH的币种
        List<Account> unableDirectConvertEthAccounts = new ArrayList<>();
        //以ETH为分母进行转换
        for (Account a : unableDirectConvertBtcAccounts) {
            if (!a.getCoinCode().equalsIgnoreCase(CoinCode.ETH)) {
                String symbol = a.getCoinCode() + "_" + CoinCode.ETH;
                ResultEntity jsonObject = JSON.parseObject(huoBiInterface.price(symbol), ResultEntity.class);
                List<CommodityModel> commodityModels = JSON.parseArray(jsonObject.getData().toString(), CommodityModel.class);
                if (null != commodityModels && commodityModels.size() > 0) {
                    CommodityModel model = commodityModels.get(0);
                    String lastPrice = model.getLastPrice();
                    if (StrUtil.isNotBlank(lastPrice)) {
                        netWorth = netWorth.add(new BigDecimal(lastPrice).multiply(a.getTotal(), MathContext.DECIMAL32));
                    } else {
                        unableDirectConvertEthAccounts.add(a);
                    }
                }
            }
        }

        //以USDT为分母进行转换
        for (Account a : unableDirectConvertEthAccounts) {
            if (!a.getCoinCode().equalsIgnoreCase(CoinCode.USDT)) {
                String symbol = a.getCoinCode() + "_" + CoinCode.USDT;
                ResultEntity jsonObject = JSON.parseObject(huoBiInterface.price(symbol), ResultEntity.class);
                List<CommodityModel> commodityModels = JSON.parseArray(jsonObject.getData().toString(), CommodityModel.class);
                if (null != commodityModels && commodityModels.size() > 0) {
                    CommodityModel model = commodityModels.get(0);
                    netWorth = netWorth.add(new BigDecimal(model.getLastPrice()).multiply(a.getTotal(), MathContext.DECIMAL32));
                }
            }
        }
        return TxUtils.removeRedundanceZeroString(netWorth);
    }


}
