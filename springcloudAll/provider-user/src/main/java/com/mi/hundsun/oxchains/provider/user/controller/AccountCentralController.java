/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.user.controller;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.MD5Utils;
import com.mi.hundsun.oxchains.base.common.utils.RSAUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.constant.Constants;
import com.mi.hundsun.oxchains.base.core.po.fn.RechargeCoin;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.base.core.po.user.UserFreeze;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.fn.MentionCoinService;
import com.mi.hundsun.oxchains.base.core.service.fn.RechargeCoinService;
import com.mi.hundsun.oxchains.base.core.service.tpl.ServiceFeeService;
import com.mi.hundsun.oxchains.base.core.service.user.UserFreezeService;
import com.mi.hundsun.oxchains.base.core.service.user.UserIdentifyService;
import com.mi.hundsun.oxchains.base.core.service.user.UserRiskControlService;
import com.mi.hundsun.oxchains.base.core.service.user.UsersService;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 枫亭
 * @date 2018-04-08 20:49.
 */
@Api(value = "账户中心相关服务", description = "AccountController Created By 枫亭 At 2018-04-08 20:49.")
@Slf4j
@RestController
@RequestMapping("/prod/account/central")
public class AccountCentralController extends GenericController {

    @Autowired
    RechargeCoinService rechargeCoinService;
    @Autowired
    UsersService usersService;
    @Autowired
    UserIdentifyService userIdentifyService;
    @Autowired
    UserFreezeService userFreezeService;
    @Autowired
    UserRiskControlService userRiskControlService;
    @Autowired
    MentionCoinService mentionCoinService;
    @Autowired
    ServiceFeeService serviceFeeService;


    @ApiOperation(value = "获取充币记录", notes = "获取我的充币记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
    })
    @PostMapping("/rechargeCoin/getByUserId")
    public ResultEntity getRechargeCoinListByUserId(@RequestParam Integer userId, @RequestParam Integer pageSize,
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
            long recordCount = rechargeCoinService.getRechargeCoinCountByUserId(userId);
            //总分页数
            long pageNum = recordCount % pageSize == 0 ? recordCount / pageSize : recordCount / pageSize + 1;
            List<RechargeCoin> list = rechargeCoinService.getRechargeCoinListByUserId(userId, pageSize, pageNumber);
            map.put("recordCount", recordCount);
            map.put("pageNum", pageNum);
            map.put("data", list);
            return ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    @ApiOperation(value = "我的认证信息", notes = "获取我的认证信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户的id", required = true, dataType = "Integer")
    })
    @PostMapping("/getUserIdentifyInfo")
    public ResultEntity getUserIdentifyInfo(@RequestParam Integer userId) {
        if (null == userId) {
            return fail("用户id参数错误");
        }
        try {
            UserIdentify userIdentify = userIdentifyService.selectOne(new UserIdentify(u -> {
                u.setUserId(userId);
                u.setDelFlag(GenericPo.DELFLAG.NO.code);
            }));
            if (null != userIdentify) {
                return ok(userIdentify);
            }
            return fail("没有数据");
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    @ApiOperation(value = "校验提币密码", notes = "校验提币密码(同时返回安全验证手机号或/邮箱及谷歌验证器状态)-用于是否弹出安全验证/")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户的id", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "mentionPwd", value = "加密后的提币密码", required = true, dataType = "String")
    })
    @RequestMapping("/checkMentionPwdForSafe")
    public ResultEntity checkMentionPwdForSafe(@RequestParam Integer userId, @RequestParam String mentionPwd) {
        if (null == userId) {
            return fail("用户id参数错误");
        }
        if (StringUtils.isBlank(mentionPwd)) {
            return fail("提币密码参数错误");
        }
        try {
            Users users = usersService.selectOne(new Users(u -> {
                u.setId(userId);
                u.setDelFlag(GenericPo.DELFLAG.NO.code);
            }));
            if (null == users) {
                return fail("用户不存在");
            }
            boolean b = false;
            if (users.getGoogleState() == Users.GOOGLESTATE.open.code) {
                b = true;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("googleState", b);//true 需要验证谷歌验证器 false 不需要验证
            //获取明文
            String mPwd = RSAUtils.decrypt(mentionPwd);
            //获得密码 + 密码盐 md5后的密码串
            String md5mPwd = MD5Utils.getMd5(mPwd, users.getMentionPwdSalt());
            if (md5mPwd.equals(users.getMentionPwd())) {
                UserIdentify userIdentifie = userIdentifyService.selectOne(new UserIdentify(u -> {
                    u.setUserId(userId);
                    u.setDelFlag(GenericPo.DELFLAG.NO.code);
                }));
                if (userIdentifie.getMobileState() == UserIdentify.MOBILESTATE.CERTIFIED.code) {
                    String hideUserName = hideUserName(users.getMobile(), Constants.MOBILE_TYPE);
                    map.put("hideUserName", hideUserName);
                    //类型1，手机号2，邮箱
                    map.put("type", Constants.MOBILE_TYPE);
                    return ok(map);
                } else if (userIdentifie.getMobileState() == UserIdentify.MOBILESTATE.CERTIFIED.code) {
                    String hideUserName = hideUserName(users.getEmail(), Constants.EMAIL_TYPE);
                    map.put("hideUserName", hideUserName);
                    //类型1，手机号2，邮箱
                    map.put("type", Constants.EMAIL_TYPE);
                    return ok(map);
                }
                return fail("用户认证信息有误");
            } else {
                return fail("提币密码不正确");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    /**
     * 隐藏用户名
     *
     * @param userName 用户名
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
            @ApiImplicitParam(name = "userId", value = "用户的id", required = true, dataType = "Integer")
    })
    @PostMapping("/checkMyMentionCoin")
    public ResultEntity checkMyMentionCoin(@RequestParam Integer userId) {
        if (null == userId) {
            return fail("用户id参数错误");
        }
        try {
            //提币是否冻结
            UserFreeze userFreeze = userFreezeService.selectOne(new UserFreeze(u -> {
                u.setUserId(userId);
                u.setDelFlag(GenericPo.DELFLAG.NO.code);
            }));
            if (userFreeze.getMentionCoinState() == UserFreeze.MENTIONCOINSTATE.FROZEN.code) {
                return fail("提币已冻结");
            }
            //实名认证
            UserIdentify userIdentify = userIdentifyService.selectOne(new UserIdentify(u -> {
                u.setUserId(userId);
                u.setDelFlag(GenericPo.DELFLAG.NO.code);
            }));
            if (null != userIdentify) {
                if (userIdentify.getRealnameState() != UserIdentify.REALNAMESTATE.CERTIFIED.code) {
                    return result("未实名认证，请先实名认证", ResultEntity.NO_REALNAME_AUTH);
                }
            } else {
                return fail();
            }
            //提币密码和googleKey设置
            Users users = usersService.selectOne(new Users(u -> {
                u.setId(userId);
                u.setDelFlag(GenericPo.DELFLAG.NO.code);
            }));
            if (StringUtils.isBlank(users.getMentionPwd())) {
                return result("请先设置提币密码", ResultEntity.MENTION_COIN_NOT_SET);
            } else if (StringUtils.isBlank(users.getGoogleKey())) {
                return result("请先绑定谷歌验证器", ResultEntity.NO_GOOGLE_BIND);
            }
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }


    @ApiOperation(value = "校验提币金额限制", notes = "校验提币金额限制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户的id", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "code", value = "提币币种", required = true, dataType = "String")
            , @ApiImplicitParam(name = "amount", value = "提币数量", required = true, dataType = "String")
    })
    @PostMapping("/checkMaxAmount")
    public ResultEntity checkMaxAmount(@RequestParam Integer userId, @RequestParam String code, @RequestParam String amount) {
        List<ServiceFee> tplFees = serviceFeeService.select(new ServiceFee(f -> {
            f.setCoinCurrency(code);
            f.setDelFlag(GenericPo.DELFLAG.NO.code);
            f.setState(ServiceFee.STATE.ENABLE.code);
        }));
        ServiceFee tplFee = null;
        if (null != tplFees && tplFees.size() > 0) {
            tplFee = tplFees.get(0);
        }
        //获取今日提币累计
        BigDecimal total = mentionCoinService.getMentionCoinByToday(userId, code);
        if (null != tplFee) {
            //校验单次提币最小限额
            if (tplFee.getOnceMinAmount().compareTo(new BigDecimal(amount)) < 0) {
                return fail("大于单次提币最小限额");
            }
            // 是否超出当日累计最大限额
            BigDecimal surplus = tplFee.getTodayMaxAmount().subtract(total);
            if (BigDecimal.valueOf(0).compareTo(surplus) < 0) {
                if (surplus.compareTo(new BigDecimal(amount)) < 0) {
                    return fail("超出今日累计提币最大限额");
                }
            } else {
                return fail("超出今日累计提币最大限额");
            }
        } else {
            return fail("用户手续费模板错误");
        }
        return ok();
    }


}
