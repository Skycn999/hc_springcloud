/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.base.core.common;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 枫亭
 * @description 交易所详细数量
 * @date 2018-05-07 21:50.
 */
public class VoDetail implements Serializable{

    private String exchangeNo;

    private BigDecimal volume;

    public String getExchangeNo() {
        return exchangeNo;
    }

    public void setExchangeNo(String exchangeNo) {
        this.exchangeNo = exchangeNo;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = new BigDecimal(volume);
    }
}
