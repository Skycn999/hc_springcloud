/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.controller.common;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.consumer.web.config.WebGenericController;
import com.mi.hundsun.oxchains.consumer.web.service.user.ExchangeInterface;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 枫亭
 * @date 2018-04-13 15:31.
 */
@RestController
@RequestMapping("/api/web/common")
public class WebController extends WebGenericController {

    @Autowired
    private ExchangeInterface exchangeInterface;

    @RequestMapping("/getSymbolConfig")
    public ResultEntity getSymbolConfig(@RequestParam String symbol) {
        if (StrUtil.isBlank(symbol)) {
            throw new BussinessException("[symbol]参数不能为空");
        }
        if (!symbol.contains("_")) {
            throw new BussinessException("[symbol]参数不合法");
        }
        ResultEntity codePairConfigResult = exchangeInterface.getCodePairConfig(symbol);
        if (codePairConfigResult.getCode() == ResultEntity.SUCCESS) {
            return ok(codePairConfigResult.getData());
        }
        return fail("未获取到交易对配置");
    }

    @ApiOperation(value = "获取交易所品种配置", notes = "获取交易所品种配置,支持配置多个")
    @PostMapping(value = "/getSymbolsConfigs")
    public ResultEntity getSymbolsConfigs(@RequestParam("symbols") String symbols) {
        if (StrUtil.isBlank(symbols)) {
            throw new BussinessException("[symbols]不能为空");
        }

        //用户选择了某个具体的交易 则只查询这个交易所的
        ResultEntity resultEntity = exchangeInterface.getSymbolsConfigs(symbols);
        if(resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        }
        return fail("未获取到对应配置信息");
    }

}
