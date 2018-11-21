/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.base.core.common;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 枫亭
 * @description 买卖档位数量&价格 model
 * @date 2018-05-07 21:50.
 */
public class VoPrModel implements Serializable {

    private String name;

    private BigDecimal volume;

    private BigDecimal price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = new BigDecimal(volume);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = new BigDecimal(price);
    }
}
