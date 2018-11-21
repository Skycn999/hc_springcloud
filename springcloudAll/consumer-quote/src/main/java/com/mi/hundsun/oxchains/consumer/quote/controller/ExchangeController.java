/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.quote.controller;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.consumer.quote.service.MotherAccountInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 枫亭
 * @description 品种相关接口
 * @date 2018-04-12 17:14.
 */
@RestController
@RequestMapping("/api/exchange")
public class ExchangeController extends GenericController {

    @Autowired
    MotherAccountInterface motherAccountInterface;

    /**
     * 按照交易所搜索
     *
     * @param exchangeNo 交易所代码
     */
    @RequestMapping("/getAccountInfoByExchange")
    public ResultEntity getAccountInfoByExchange(@RequestParam String exchangeNo) {
        if (StringUtils.isBlank(exchangeNo)) {
            return fail("[交易所]参数错误");
        }
        ResultEntity resultEntity = motherAccountInterface.getAccountInfoByExchange(exchangeNo);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        } else {
            return fail("没有查询到对应交易所母账号信息");
        }

    }

}
