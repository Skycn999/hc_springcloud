/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.user.controller;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.enums.ExchangeEnum;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.po.quote.CodePairConfig;
import com.mi.hundsun.oxchains.base.core.service.exchange.MotherAccountService;
import com.mi.hundsun.oxchains.base.core.service.quote.CodePairConfigService;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 枫亭
 * @date 2018-04-08 20:58.
 */
@Api(value = "用户模块相关服务", description = "RegistLoginController Created By 枫亭 at 2018-04-08 20:58")
@RestController
@RequestMapping("/prod/exchange")
public class ExchangeController extends GenericController {

    @Autowired
    private MotherAccountService motherAccountService;

    @Autowired
    private CodePairConfigService codePairConfigService;

    @ApiOperation(value = "交易所母账号筛选", notes = "通过交易所或者币种获取对应的交易所母账号返回账号是一个或者多个")
    @PostMapping(value = "/findMotherAccounts")
    public ResultEntity findMotherAccounts(@RequestParam("exchangeNo") String exchangeNo, @RequestParam("code") String code) {
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[exchangeNo]不能为空");
        }

        List<MotherAccountInfoModel> accountInfos = new ArrayList<>();
        //获取交易母账号
        if (exchangeNo.equals(ExchangeEnum.ALL.getCode())) {
            //前端用户10档行情选择的是全部交易所则需要每个交易所查询一个最优的账号出来
            accountInfos = motherAccountService.findByCoinCode(code);
        } else {
            //用户选择了某个具体的交易所 则只查询这个交易所的
            MotherAccountInfoModel model = motherAccountService.findByExchangeNoAndCoinCode(exchangeNo, code);
            accountInfos.add(model);
        }
        //通过交易所查询
        if (StrUtil.isBlank(code)) {
            MotherAccountInfoModel byExchangeNo = motherAccountService.findByExchangeNo(exchangeNo);
            accountInfos.add(byExchangeNo);
        }
        //获取交易母账号
        if (null != accountInfos && accountInfos.size() > 0) {
            //前端用户10档行情选择的是全部交易所则需要每个交易所查询一个最优的账号出来
            return ok(JSON.toJSONString(accountInfos));
        }
        return fail();
    }

    @ApiOperation(value = "交易所母账号筛选", notes = "通过交易所编号、母账号、币种查询母账号信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "exchangeNo", value = "交易所编号", required = true, dataType = "String"),
    })
    @PostMapping(value = "/findMotherAccountByExNoAndAccountName")
    public ResultEntity findMotherAccountByExNoAndAccountName(@RequestParam("exchangeNo") String exchangeNo,
                                               @RequestParam("accountName") String accountName) {
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[exchangeNo]不能为空");
        }
        if (StrUtil.isBlank(accountName)) {
            throw new BussinessException("[accountName]不能为空");
        }

        //获取交易母账号
        MotherAccountInfoModel accountInfo = motherAccountService.findByExNoAndAccountName(exchangeNo, accountName);
        if (null != accountInfo) {
            return ok(JSON.toJSONString(accountInfo));
        }
        return fail();
    }

    @ApiOperation(value = "获取可用的交易所母账号", notes = "通过交易所或者币种获取对应的交易所母账号返回账号是一个或者多个")
    @PostMapping(value = "/findMotherAccount")
    public MotherAccountInfoModel findMotherAccount(@RequestParam("exchangeNo") String exchangeNo, @RequestParam("code") String code) {
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[exchangeNo]不能为空");
        }
        if (StrUtil.isBlank(code)) {
            throw new BussinessException("[code]不能为空");
        }

        //用户选择了某个具体的交易 则只查询这个交易所的
        return motherAccountService.findByExchangeNoAndCoinCode(exchangeNo, code);
    }

    @ApiOperation(value = "获取交易所品种配置", notes = "获取交易所品种配置")
    @PostMapping(value = "/getCodePairConfig")
    public ResultEntity getCodePairConfig(@RequestParam("currencyPair") String currencyPair) {
        if (StrUtil.isBlank(currencyPair)) {
            throw new BussinessException("[currencyPair]不能为空");
        }

        //用户选择了某个具体的交易 则只查询这个交易所的
        CodePairConfig codePairConfig = codePairConfigService.getCodePairConfig(currencyPair);
        return null != codePairConfig ? ok(codePairConfig) : fail("No This CurrencyPair");
    }

    @ApiOperation(value = "获取交易所品种配置", notes = "获取交易所品种配置,支持配置多个")
    @PostMapping(value = "/getSymbolsConfigs")
    public ResultEntity getSymbolsConfigs(@RequestParam("symbols") String symbols) {
        if (StrUtil.isBlank(symbols)) {
            throw new BussinessException("[symbols]不能为空");
        }

        //用户选择了某个具体的交易 则只查询这个交易所的
        Map<String ,CodePairConfig> codePairConfigMaps = codePairConfigService.getSymbolsConfigs(symbols);
        return null != codePairConfigMaps ? ok(codePairConfigMaps) : fail("No This symbols");
    }
}
