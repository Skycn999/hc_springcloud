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
import com.mi.hundsun.oxchains.consumer.quote.service.*;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 枫亭
 * @date 2018-04-11 15:32.
 */
@RestController
@RequestMapping("/api/quote")
public class QuoteController extends GenericController {

    @Autowired
    QuoteHuoBiInterface quoteHuoBiInterface;
    @Autowired
    QuoteBiAnInterface quoteBiAnInterface;
    @Autowired
    QuoteBitFinexInterface quoteBitFinexInterface;
    @Autowired
    QuoteOkexInterface quoteOkexInterface;
    @Autowired
    CommodityInterface commodityInterface;

    @RequestMapping("/price")
    public ResultEntity price(@RequestParam String symbol) {
        if (StringUtils.isBlank(symbol)) {
            return fail("参数错误");
        } else {
            String json = quoteHuoBiInterface.price(symbol);
            ResultEntity jsonObject = JSON.parseObject(json, ResultEntity.class);
            if (jsonObject.getCode() == ResultEntity.SUCCESS) {
                return jsonObject;
            } else {
                return fail(jsonObject.getMessage());
            }
        }
    }

    @RequestMapping("/trend")
    public ResultEntity trend(@RequestParam String symbol, @RequestParam String size) {
        if (StringUtils.isBlank(symbol) || StringUtils.isBlank(size)) {
            return fail("参数错误");
        } else {
            String json = quoteHuoBiInterface.trend(symbol, size);
            ResultEntity jsonObject = JSON.parseObject(json, ResultEntity.class);
            if (jsonObject.getCode() == ResultEntity.SUCCESS) {
                return jsonObject;
            } else {
                return fail(jsonObject.getMessage());
            }
        }
    }

    @RequestMapping("/kline")
    public ResultEntity kline(@RequestParam String symbol, @RequestParam String kline_type, @RequestParam String size) {
        if (StringUtils.isBlank(symbol) || StringUtils.isBlank(size)) {
            return fail("参数错误");
        } else {
            String json = quoteHuoBiInterface.kline(symbol, kline_type, size);
            ResultEntity jsonObject = JSON.parseObject(json, ResultEntity.class);
            if (jsonObject.getCode() == ResultEntity.SUCCESS) {
                return jsonObject;
            } else {
                return fail(jsonObject.getMessage());
            }
        }
    }

    /**
     * 分笔成交
     *
     * @param symbol 代码
     * @param size   条数
     * @return ResultEntity
     */
    @RequestMapping("/tick")
    public ResultEntity tick(@RequestParam String symbol, @RequestParam String size, String exchangeType) {
        if (StringUtils.isBlank(symbol) || StringUtils.isBlank(size)) {
            return fail("参数错误");
        }
        if (StringUtils.isBlank(exchangeType)) {
            exchangeType = "huobi";
        }
        String json;
        switch (exchangeType) {
            case "huobi":
                json = quoteHuoBiInterface.tick(symbol, size);
                break;
            case "bian":
                json = quoteBiAnInterface.tick(symbol, size);
                break;
            case "bitfinex":
                json = quoteBitFinexInterface.tick(symbol, size);
                break;
            case "okex":
                json = quoteOkexInterface.tick(symbol, size);
                break;
            default:
                json = quoteHuoBiInterface.tick(symbol, size);
                break;
        }
        ResultEntity jsonObject = JSON.parseObject(json, ResultEntity.class);
        if (jsonObject.getCode() == ResultEntity.SUCCESS) {
            return jsonObject;
        } else {
            return fail(jsonObject.getMessage());
        }

    }

    /**
     * 买卖10档
     *
     * @param symbol       代码
     * @param exchangeType 交易所名称 不传或传空为聚合行情
     * @return ResultEntity
     */
    @RequestMapping("/depth")
    public ResultEntity depth(@RequestParam String symbol, @RequestParam String exchangeType) {
        if (StringUtils.isBlank(symbol)) {
            return fail("参数错误");
        } else {
            if (StrUtil.isBlank(exchangeType)) {
                exchangeType = "all";
            }
            String json;
            switch (exchangeType) {
                case "huobi":
                    json = quoteHuoBiInterface.depth(symbol, exchangeType);
                    break;
                case "bian":
                    json = quoteBiAnInterface.depth(symbol, exchangeType);
                    break;
                case "bitfinex":
                    json = quoteBitFinexInterface.depth(symbol, exchangeType);
                    break;
                case "okex":
                    json = quoteOkexInterface.depth(symbol, exchangeType);
                    break;
                default:
                    json = quoteHuoBiInterface.depth(symbol, "");
                    break;
            }
            ResultEntity jsonObject = JSON.parseObject(json, ResultEntity.class);
            if (jsonObject.getCode() == ResultEntity.SUCCESS) {
                return ok(jsonObject.getData());
            } else {
                return fail(jsonObject.getMessage());
            }
        }
    }

    /**
     * app主页获取主要币种行情信息
     *
     * @return ResultEntity
     */
    @RequestMapping("/getMainCodeInfo")
    public ResultEntity getMainCodeInfo() {
        ResultEntity displayOnAppCodes = commodityInterface.getDisplayOnAppCodes();
        if (displayOnAppCodes.getCode() == ResultEntity.SUCCESS) {
            String symbols = displayOnAppCodes.getData().toString();
            String json = quoteHuoBiInterface.price(symbols);
            ResultEntity jsonObject = JSON.parseObject(json, ResultEntity.class);
            if (jsonObject.getCode() == ResultEntity.SUCCESS) {
                List<CommodityModel> models = JSON.parseArray(((JSONArray) jsonObject.getData()).toJSONString(), CommodityModel.class);
                return ok(models);
            } else {
                return fail(jsonObject.getMessage());
            }
        }
        return fail("no infos");
    }

    /**
     * 涨幅榜
     */
    @RequestMapping("/increaseList")
    public ResultEntity increaseList() {
        ResultEntity resultEntity = commodityInterface.getAllValidCodes();
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            String symbols = resultEntity.getData().toString();
            String json = quoteHuoBiInterface.price(symbols);
            ResultEntity jsonObject = JSON.parseObject(json, ResultEntity.class);
            if (jsonObject.getCode() == ResultEntity.SUCCESS) {
                List<CommodityModel> models = JSON.parseArray(((JSONArray) jsonObject.getData()).toJSONString(), CommodityModel.class);
                models.sort(new MyComparator("DESC", "priceIncrease"));
                return ok(models);
            }
        }
        return fail(resultEntity.getMessage());
    }

    /**
     * 跌幅榜
     */
    @RequestMapping("/dropList")
    public ResultEntity dropList() {
        ResultEntity resultEntity = commodityInterface.getAllValidCodes();
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            String symbols = resultEntity.getData().toString();
            String json = quoteHuoBiInterface.price(symbols);
            ResultEntity jsonObject = JSON.parseObject(json, ResultEntity.class);
            if (jsonObject.getCode() == ResultEntity.SUCCESS) {
                List<CommodityModel> models = JSON.parseArray(((JSONArray) jsonObject.getData()).toJSONString(), CommodityModel.class);
                models.sort(new MyComparator("ASC", "priceIncrease"));
                return ok(models);
            }
        }
        return fail(resultEntity.getMessage());
    }


}
