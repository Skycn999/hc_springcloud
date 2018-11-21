/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.base.core.util;

import com.mi.hundsun.oxchains.base.core.constant.CoinCode;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author 枫亭
 * @description 交易相关工具类
 * @date 2018-05-22 15:54.
 */
@Slf4j
public class TxUtils {

    public static boolean isMainCoinCode(String code) {
        return CoinCode.BTC.equalsIgnoreCase(code) || CoinCode.ETH.equalsIgnoreCase(code);
    }


    public static BigDecimal removeRedundanceZeroString(String str) {
        return new BigDecimal(new BigDecimal(str).stripTrailingZeros().toPlainString());
    }

    public static BigDecimal removeRedundanceZeroString(BigDecimal str) {
        return new BigDecimal(str.stripTrailingZeros().toPlainString());
    }

    public static String getMuCode(String currencyCode) {
        if (!currencyCode.contains("_")) {
            log.error("[" + currencyCode + "]交易对不合法");
            return "";
        }
        return currencyCode.split("_")[1];
    }

    public static String getZiCode(String currencyCode) {
        if (!currencyCode.contains("_")) {
            log.error("[" + currencyCode + "]交易对不合法");
            return "";
        }
        return currencyCode.split("_")[0];
    }
}
