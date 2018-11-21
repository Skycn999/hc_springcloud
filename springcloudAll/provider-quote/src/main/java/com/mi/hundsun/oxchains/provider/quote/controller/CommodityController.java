/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.quote.controller;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.quote.CodePairConfig;
import com.mi.hundsun.oxchains.base.core.service.quote.CodePairConfigService;
import com.mi.hundsun.oxchains.base.core.service.quote.CommodityService;
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

/**
 * @author 枫亭
 * @date 2018-04-12 17:29.
 */
@Api("行情配置相关服务")
@Slf4j
@RestController
@RequestMapping("/quote/commodity")
public class CommodityController extends GenericController {

    @Autowired
    CommodityService commodityService;

    @Autowired
    CodePairConfigService codePairConfigService;

    @ApiOperation(value = "查询分区币种")
    @PostMapping(value = "/getSymbolsByBaseCodeAndCode")
    public ResultEntity getSymbolsByBaseCodeAndCode(@RequestParam String partition,
                                                    @RequestParam String code,
                                                    @RequestParam String orderColumn,
                                                    @RequestParam String orderType,
                                                    @RequestParam int pageNumber,
                                                    @RequestParam int pageSize) throws BussinessException {
        if (StrUtil.isBlank(partition)) {
            throw new BussinessException("[partition]不能为空");
        }
        List<CodePairConfig> pairConfigs = codePairConfigService.pageByCodeAndBaseCode(partition, code, orderColumn, orderType, pageNumber, pageSize);
        String symbols = this.getSymbols(pairConfigs);
        if (StrUtil.isNotBlank(symbols)) {
            return ok(symbols);
        } else {
            return fail("No This Commodity");
        }
    }

    @ApiOperation(value = "查询分区币种")
    @PostMapping(value = "/getByCode")
    public ResultEntity getByCode(@RequestParam String partition,
                                  @RequestParam String code) throws BussinessException {
        List<CodePairConfig> commodities = codePairConfigService.findByCodeAndBaseCode(partition, code);
        String symbols = this.getSymbols(commodities);
        if (StrUtil.isNotBlank(symbols)) {
            return ok(symbols);
        } else {
            return fail("No This Commodity");
        }
    }

    @ApiOperation(value = "通过交易所查询交易对")
    @PostMapping(value = "/getByExchange")
    public ResultEntity getByExchange(@RequestParam String exchange) throws BussinessException {
        List<CodePairConfig> commodities = codePairConfigService.getByExchange(exchange);
        String symbols = this.getSymbols(commodities);
        if (StrUtil.isNotBlank(symbols)) {
            return ok(symbols);
        } else {
            return fail("No This Commodity");
        }
    }

    @ApiOperation(value = "通过code查询交易对")
    @PostMapping(value = "/searchForApp")
    public ResultEntity searchForApp(@RequestParam String code) throws BussinessException {
        List<CodePairConfig> commodities = codePairConfigService.searchForApp(code);
        String symbols = this.getSymbols(commodities);
        if (StrUtil.isNotBlank(symbols)) {
            return ok(symbols);
        } else {
            return fail("No This Commodity");
        }
    }

    @ApiOperation(value = "APP首页查询主流币种行情")
    @PostMapping(value = "/getDisplayOnAppCodes")
    public ResultEntity getDisplayOnAppCodes() throws BussinessException {
        List<CodePairConfig> commodities = codePairConfigService.getDisplayOnAppCodes();
        String symbols = this.getSymbols(commodities);
        if (StrUtil.isNotBlank(symbols)) {
            return ok(symbols);
        } else {
            return fail("No This Commodity");
        }
    }

    @ApiOperation(value = "获取所有可用品种")
    @PostMapping(value = "/getAllValidCodes")
    public ResultEntity getAllValidCodes() throws BussinessException {
        List<CodePairConfig> commodities = codePairConfigService.getAllValidCodes();
        String symbols = this.getSymbols(commodities);
        if (StrUtil.isNotBlank(symbols)) {
            return ok(symbols.toUpperCase());
        } else {
            return fail("No This Commodity");
        }
    }

    /**
     * 获取货币对串  以逗号分隔
     *
     * @param commodities 代码对列表
     * @return 货币对串
     */
    private String getSymbols(List<CodePairConfig> commodities) {
        if (null == commodities || commodities.size() < 1) {
            return "";
        }
        StringBuilder symbols = new StringBuilder();
        String pair;
        for (CodePairConfig c : commodities) {
            pair = c.getCode() + "_" + c.getBaseCode();
            symbols.append(pair).append(",");
        }
        return symbols.substring(0, symbols.length() - 1).toLowerCase();
    }
}
