/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.quote.controller;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccount;
import com.mi.hundsun.oxchains.base.core.service.exchange.MotherAccountService;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.mi.hundsun.oxchains.base.common.entity.ResultEntity.fail;

/**
 * @author 枫亭
 * @description TODO
 * @date 2018-04-12 17:29.
 */
@Api("刚请配置相关服务")
@Slf4j
@RestController
@RequestMapping("/quote/exchange")
public class ExchangeController extends GenericController {

    @Autowired
    MotherAccountService motherAccountService;

    @ApiOperation(value = "通过交易所查询交易对")
    @PostMapping(value = "/getAccountInfoByExchange")
    public ResultEntity getAccountInfoByExchange(@RequestParam String exchangeNo) throws BussinessException {
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[exchangeNo]不能为空");
        }
        List<MotherAccount> accounts = motherAccountService.getApiInfoByExchange(exchangeNo);
        if(null == accounts) {
            return fail("未查询到对应交易所母账号信息");
        }
        return ok(JSON.toJSONString(accounts));

    }


}
