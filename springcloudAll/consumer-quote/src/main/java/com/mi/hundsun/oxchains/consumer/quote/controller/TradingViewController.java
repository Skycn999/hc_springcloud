/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.quote.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.common.CandleLineModel;
import com.mi.hundsun.oxchains.base.core.common.CommodityModel;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.consumer.quote.service.QuoteHuoBiInterface;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * Trading View 图表库
 *
 * @author 枫亭
 * @date 2018-04-28 17:37
 */
@Api(description = "trading view 图标库相关接口")
@Slf4j
@RestController
@RequestMapping("/api/quote/tv")
public class TradingViewController extends GenericController {

    @Autowired
    private QuoteHuoBiInterface quoteHuoBiInterface;

    private static final String[] SUPPORTED_RESOLUTIONS = new String[]{"1", "5", "15", "30", "60", "1D", "W", "M"};


    @ApiOperation(value = "Datafeed 配置数据", notes = "Datafeed 配置数据")
    @RequestMapping(value = "config", method = RequestMethod.GET)
    public Object config() {
        // Datafeed 配置数据
        Map<String, Object> result = new HashMap<>();
        // 一个交易所数组。 Exchange是一个对象{value, name, desc}
        result.put("exchanges", "");
        // 一个表示服务器支持的分辨率数组，分辨率可以是数字或字符串。 如果分辨率是一个数字，它被视为分钟数。 字符串可以是“*D”，“*W”，“_M”（_的意思是任何数字）
        result.put("supported_resolutions", SUPPORTED_RESOLUTIONS);
        result.put("supports_group_request", false);
        // 布尔值来标识您的 datafeed 是否支持在K线上显示标记。
        result.put("supports_marks", false);
        result.put("supports_search", true);
        // 将此设置为true假如您的datafeed提供服务器时间（unix时间）。 它用于调整时间刻度上的价格比例。
        result.put("supports_time", true);
        // 布尔值来标识您的 datafeed 是否支持时间刻度标记。
        result.put("supports_timescale_marks", false);
        // 一个商品类型过滤器数组。该商品类型过滤器是个对象{name, value}
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "ALL types");
        map.put("value", "");
        result.put("symbols_types", map);

        return result;

    }

    @ApiOperation(value = "服务器时间", notes = "服务器时间")
    @RequestMapping(value = "time", method = RequestMethod.GET)
    public Object time() {
        return System.currentTimeMillis();
    }


    @ApiOperation(value = "商品信息", notes = "商品信息")
    @RequestMapping(value = "symbol_info", method = RequestMethod.GET)
    public Object symbol_info(@ApiParam("商品代码") @RequestParam final String group) {

        return group;
    }

    @ApiOperation(value = "商品解析", notes = "商品解析")
    @RequestMapping(value = "symbols", method = RequestMethod.GET)
    public Object symbols(@ApiParam("商品代码") @RequestParam final String symbol) {
        if (!symbol.contains("_")) {
            throw new BussinessException("商品代码不合法");
        }
        // Datafeed 配置数据
        Map<String, Object> result = new HashMap<>();
        // 商品说明
        result.put("description", symbol.split("_")[0].toUpperCase());
        // 交易所
        result.put("exchange-listed", "");
        // 交易所
        result.put("exchange-traded", "");
        // 商品是否具有日内（分钟）历史数据
        result.put("has_intraday", true);
        // 商品是否拥有成交量数据
        result.put("has_no_volume", false);
        // 最小波动
        result.put("minmov", 1);
        // 最小波动
        result.put("minmov2", 0);
        // 商品名称
        result.put("name", symbol.split("_")[1].toUpperCase());
        // 价格精度
        result.put("pointvalue", 1);
        // 价格精度
        int pricescale = 10;
        String price = quoteHuoBiInterface.price(symbol);
        ResultEntity jsonObject = JSON.parseObject(price, ResultEntity.class);
        if (jsonObject.getCode() == ResultEntity.SUCCESS) {
            List<CommodityModel> models = JSON.parseArray(((JSONArray) jsonObject.getData()).toJSONString(), CommodityModel.class);
            try {
                int length = models.get(0).getLastPrice().split("\\.")[1].length();
                switch (length) {
                    case 1:
                        pricescale = 10;
                        break;
                    case 2:
                        pricescale = 100;
                        break;
                    case 3:
                        pricescale = 1000;
                        break;
                    case 4:
                        pricescale = 10000;
                        break;
                    case 5:
                        pricescale = 100000;
                        break;
                    case 6:
                        pricescale = 1000000;
                        break;
                    case 7:
                        pricescale = 10000000;
                        break;
                    case 8:
                        pricescale = 100000000;
                        break;
                    case 9:
                        pricescale = 1000000000;
                        break;

                }
            } catch (Exception e) {
                pricescale = 10;
            }
        }
        result.put("pricescale", pricescale);
        // 商品交易时间 例如 0900-1630 | 0900-1400
        result.put("session", "0000-2359");
        // 商品的分辨率 例如 ["1","5","15","30","60","1D","1W","1M"]
        result.put("supported_resolutions", SUPPORTED_RESOLUTIONS);
        // 它是您的商品体系中此商品的唯一标识符
        result.put("ticker", symbol);
        // 这个商品的交易所时区
        result.put("timezone", "Asia/Shanghai");
        // 仪表的可选类型 可能的值：stock, index, forex, futures, bitcoin, expression, spread, cfd 或其他字符串
        result.put("type", "bitcoin");

        return result;
    }


    @ApiOperation(value = "K线柱", notes = "K线柱")
    @RequestMapping(value = "history", method = RequestMethod.GET)
    public Object history(@ApiParam("商品代码") @RequestParam String symbol,
                          @ApiParam("开始时间") @RequestParam String from,
                          @ApiParam("结束时间") @RequestParam String to,
                          @ApiParam("分辨率") @RequestParam String resolution,
                          @ApiParam("条数") String size) {
        if (StrUtil.isBlank(size)) {
            size = "800";
        }

        if (StrUtil.isBlank(symbol)) {
            throw new BussinessException("[商品代码]不能为空");
        }

        if (StrUtil.isBlank(resolution)) {
            throw new BussinessException("[分辨率]不能为空");
        }

        switch (resolution) {
            case "1":
                resolution = "1min";
                break;
            case "5":
                resolution = "5min";
                break;
            case "15":
                resolution = "15min";
                break;
            case "30":
                resolution = "30min";
                break;
            case "60":
                resolution = "60min";
                break;
            case "1D":
                resolution = "1day";
                break;
            case "D":
                resolution = "1day";
                break;
            case "W":
                resolution = "1week";
                break;
            case "M":
                resolution = "1mon";
                break;
            default:
                throw new BussinessException("k线类型错误");
        }


        String json = quoteHuoBiInterface.kline(symbol, resolution, size);
        ResultEntity jsonObject = JSON.parseObject(json, ResultEntity.class);
        //时间戳数组
        List<Long> tarr = new ArrayList<>();
        //最新价数组
        List<BigDecimal> carr = new ArrayList<>();
        //开盘价数组
        List<BigDecimal> oarr = new ArrayList<>();
        //最高价数组
        List<BigDecimal> harr = new ArrayList<>();
        //最低价数组
        List<BigDecimal> larr = new ArrayList<>();
        //交易量数组
        List<BigDecimal> varr = new ArrayList<>();
        // 历史K线柱数据
        Map<String, Object> result = new HashMap<>();
        if (jsonObject.getCode() == ResultEntity.SUCCESS) {
            List<CandleLineModel> candleLineModels = JSON.parseArray(jsonObject.getData().toString(), CandleLineModel.class);
            if (null != candleLineModels && candleLineModels.size() > 0) {
                for (CandleLineModel model : candleLineModels) {
                    if(null == model.getTimestamp() || model.getTimestamp().length() < 3) {
                        continue;
                    }
                    tarr.add(Long.parseLong(model.getTimestamp().substring(0, model.getTimestamp().length() - 3)));
                    carr.add(new BigDecimal(model.getClose_price()));
                    oarr.add(new BigDecimal(model.getOpen_price()));
                    harr.add(new BigDecimal(model.getHigh_price()));
                    larr.add(new BigDecimal(model.getLow_price()));
                    varr.add(new BigDecimal(model.getBusiness_amount()));
                }
                // 状态码。 预期值:ok|error|no_data
                result.put("s", "ok");
            } else {
                result.put("s", "no_data");
            }
        } else {
            result.put("s", "error");
        }

        result.put("t", tarr.toArray());
        result.put("c", carr.toArray());
        result.put("o", oarr.toArray());
        result.put("l", larr.toArray());
        result.put("h", harr.toArray());
        result.put("v", varr.toArray());
        return result;
    }
}

