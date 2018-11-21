/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.user.controller;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import com.mi.hundsun.oxchains.base.core.po.user.UserAddress;
import com.mi.hundsun.oxchains.base.core.service.fn.MentionCoinService;
import com.mi.hundsun.oxchains.base.core.service.user.UserAddressService;
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
import java.util.*;

/**
 * @author 枫亭
 * @date 2018-04-08 20:49.
 */
@Api(value = "提币模块", description = "FnMentionCoinController Created By 枫亭 At 2018-04-22 22:09.")
@Slf4j
@RestController
@RequestMapping("/prod/fn/mentionCoin")
public class FnMentionCoinController extends GenericController {

    @Autowired
    MentionCoinService mentionCoinService;
    @Autowired
    UserAddressService userAddressService;

    @ApiOperation(value = "获取用户绑定的提币地址", notes = "获取用户绑定的提币地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "code", value = "币种", required = true, dataType = "String")
    })
    @PostMapping(value = "/findMyMcAddressByCode")
    public ResultEntity findMyMcAddressByCode(@RequestParam Integer userId, @RequestParam String code) {
        if (null == userId) {
            return fail("用户id不能为空");
        }
        List<UserAddress> userAddressList = userAddressService.findMyMcAddressByCode(userId, code);
        List<UserAddress> list = new ArrayList<>();
        for (UserAddress u: userAddressList) {
            UserAddress address = new UserAddress();
            address.setAddress(u.getAddress());
            list.add(address);
        }
        return ok(JSON.toJSONString(list));
    }


    @ApiOperation(value = "校验用户输入的提币地址", notes = "校验用户输入的提币地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "address", value = "提币地址", required = true, dataType = "String")
    })
    @PostMapping(value = "/checkAddress")
    public boolean checkAddress(@RequestParam Integer userId, @RequestParam String address) {
        List<UserAddress> userAddresses = userAddressService.select(new UserAddress(u -> {
            u.setUserId(userId);
            u.setAddress(address);
            u.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        return null != userAddresses && userAddresses.size() > 0;
    }


    @ApiOperation(value = "获取提币记录", notes = "获取我的提币记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
    })
    @PostMapping("/myMentionCoinLog")
    public ResultEntity myMentionCoinLog(@RequestParam Integer userId, @RequestParam Integer pageSize,
                                         @RequestParam Integer pageNumber) {
        if (null == pageSize) {
            return fail("[pageSize]不能为空");
        }
        if (null == pageNumber) {
            return fail("[pageNumber]不能为空");
        }
        if (null == userId || 0 == userId) {
            return fail("[userId]不能为空");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            long recordCount = mentionCoinService.myMentionCoinLogCount(userId);
            //总分页数
            int pageNum = (int) recordCount / pageSize + (recordCount % pageSize > 0 ? 1 : 0);
            List<MentionCoin> list = mentionCoinService.myMentionCoinLog(userId, pageSize, pageNumber);
            map.put("recordCount", recordCount);
            map.put("pageNum", pageNum);
            map.put("data", list);
            return ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }


    @ApiOperation(value = "新增提币记录", notes = "新增提币记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "address", value = "提币地址", required = true, dataType = "String")
            , @ApiImplicitParam(name = "coin", value = "币种", required = true, dataType = "String")
            , @ApiImplicitParam(name = "amount", value = "提币数量", required = true, dataType = "String")
            , @ApiImplicitParam(name = "serviceFee", value = "提币手续费", required = true, dataType = "String")
    })
    @PostMapping(value = "/addMentionCoin")
    public ResultEntity addMentionCoin(@RequestParam Integer userId, @RequestParam String address,
                                       @RequestParam String coin, @RequestParam String amount, @RequestParam String serviceFee) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(address) || StringUtils.isBlank(coin) || StringUtils.isBlank(amount) || StringUtils.isBlank(serviceFee)) {
            return fail("必填参数不能为空");
        }
        try {
            MentionCoin mentionCoin = new MentionCoin();
            mentionCoin.setUserId(userId);
            mentionCoin.setUuid(RandomUtils.randomCustomUUID());
            mentionCoin.setOrderNo(OrderNoUtils.getSerialNumber());
            mentionCoin.setUserMentionAddr(address);
            mentionCoin.setCoinCurrency(coin);
            mentionCoin.setAmount(new BigDecimal(amount));
            mentionCoin.setServiceFee(new BigDecimal(serviceFee));
            mentionCoin.setState(MentionCoin.STATE.PENDING.code);
            mentionCoin.setCreateTime(new Date());
            mentionCoinService.insert(mentionCoin);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

}
