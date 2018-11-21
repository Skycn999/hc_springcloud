/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.base.core.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author 枫亭
 * @description k线柱
 * @date 2018-04-15 14:20.
 */
public class CandleLineModel implements Serializable {

    @Getter
    @Setter
    private String close_price;
    @Getter
    @Setter
    private String business_amount;
    @Getter
    @Setter
    private String high_price;
    @Getter
    @Setter
    private String open_price;
    @Getter
    @Setter
    private String low_price;
    @Getter
    @Setter
    private String timestamp;

}
