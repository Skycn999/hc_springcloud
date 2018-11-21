/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.quote.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.common.CommodityModel;
import com.mi.hundsun.oxchains.base.core.common.MyComparator;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.consumer.quote.service.CommodityInterface;
import com.mi.hundsun.oxchains.consumer.quote.service.QuoteHuoBiInterface;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 枫亭
 * @description 品种相关接口
 * @date 2018-04-12 17:14.
 */
@RestController
@RequestMapping("/api/commo")
public class CommodityController extends GenericController {

    @Autowired
    CommodityInterface commodityInterface;
    @Autowired
    QuoteHuoBiInterface quoteHuoBiInterface;

    /**
     * 获取某个分区下的所有币种信息 默认首先获取20条
     *
     * @param partition  分区代码 如usdt
     * @param pageNumber 页码 如1
     */
    @RequestMapping("/getSymbols")
    public ResultEntity getSymbols(@RequestParam String partition, String code, @RequestParam int pageNumber, String orderColumn, String orderType) {
        if (StrUtil.isBlank(orderColumn)) {
            orderColumn = "create_time";
        }
        if (!orderColumn.equals("create_time") && !orderColumn.equals("code") && !orderColumn.equals("priceIncrease") && !orderColumn.equals("lastPrice")) {
            orderColumn = "code";
        }

        if (StrUtil.isBlank(orderType)) {
            orderType = "DESC";
        }

        if (!orderType.equals("DESC") && !orderType.equals("ASC")) {
            return fail("排序类型错误");
        }

        if (StringUtils.isBlank(partition)) {
            partition = "USDT";
        }
        int pageSize = 20;
        if (StringUtils.isBlank(pageNumber)) {
            pageNumber = 1;
        }
        ResultEntity symbolsResult;
        if (!orderColumn.equals("create_time") && !orderColumn.equals("code")) {
            symbolsResult = commodityInterface.getSymbolsByBaseCodeAndCode(partition, code, "create_time", orderType, pageNumber, pageSize);
        } else {
            symbolsResult = commodityInterface.getSymbolsByBaseCodeAndCode(partition, code, orderColumn, orderType, pageNumber, pageSize);
        }
        String symbols;
        if (symbolsResult.getCode() == ResultEntity.SUCCESS) {
            symbols = symbolsResult.getData().toString();
        } else {
            return fail("未获取到交易对信息");
        }
        String json = quoteHuoBiInterface.price(symbols);
        ResultEntity jsonObject = JSON.parseObject(json, ResultEntity.class);
        if (jsonObject.getCode() == ResultEntity.SUCCESS) {
            List<CommodityModel> models = JSON.parseArray(((JSONArray) jsonObject.getData()).toJSONString(), CommodityModel.class);
            if (orderColumn.equals("priceIncrease") || orderColumn.equals("lastPrice")) {
                models.sort(new MyComparator(orderType, orderColumn));
            }
            return ok(models);
        } else {
            return fail(jsonObject.getMessage());
        }

    }

    /**
     * 搜索币种配置 用户app端
     *
     * @param code 分区代码 如EOS
     */
    @RequestMapping("/searchForApp")
    public ResultEntity searchForApp(String code) {
        if (StrUtil.isBlank(code)) {
            throw new BussinessException("参数[code]缺失");
        }

        ResultEntity resultEntity = commodityInterface.searchForApp(code);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        } else {
            return fail(resultEntity.getMessage());
        }
    }

    /**
     * 模糊搜索分区下的币种
     *
     * @param partition 分区代码
     * @param code      币种
     */
    @RequestMapping("/getByCode")
    public ResultEntity getByCode(@RequestParam String partition, @RequestParam String code) {
        if (StringUtils.isBlank(code)) {
            return fail("参数错误");
        } else {
            ResultEntity resultEntity = commodityInterface.getByCode(partition, code);
            if (resultEntity.getCode() == ResultEntity.SUCCESS) {
                String symbols = resultEntity.getData().toString();
                String json = quoteHuoBiInterface.price(symbols.toLowerCase());
                ResultEntity jsonObject = JSON.parseObject(json, ResultEntity.class);
                if (jsonObject.getCode() == ResultEntity.SUCCESS) {
                    List<CommodityModel> models = JSON.parseArray(((JSONArray) jsonObject.getData()).toJSONString(), CommodityModel.class);
                    return ok(models);
                } else {
                    return fail(jsonObject.getMessage());
                }
            } else {
                return fail("没有此币种");
            }
        }
    }

    /**
     * 按照交易所搜索
     *
     * @param exchange 分区代码
     */
    @RequestMapping("/getByExchange")
    public ResultEntity getByExchange(@RequestParam String exchange) {
        if (StringUtils.isBlank(exchange)) {
            return fail("[交易所]参数错误");
        }
        ResultEntity resultEntity = commodityInterface.selectByExchange(exchange);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        } else {
            return fail("没有此币种");
        }

    }

}
