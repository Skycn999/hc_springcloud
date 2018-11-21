/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.base.core.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author 枫亭
 * @description TODO
 * @date 2018-04-15 14:20.
 */
public class CommodityModel implements Serializable {

    @Getter
    @Setter
    private String symbol;
    @Getter
    @Setter
    private String lastPrice;
    @Getter
    @Setter
    private String priceIncrease;
    @Getter
    @Setter
    private String businessAmount;
    @Getter
    @Setter
    private String highPrice;
    @Getter
    @Setter
    private String lowPrice;

}
