/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.base.core.common;

import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 枫亭
 * @description 交易子单model
 * @date 2018-05-07 21:50.
 */
public class SubOrderModel implements Serializable{

    @Getter
    @Setter
    private String exchangeNo;

    @Getter
    @Setter
    private BigDecimal volume;

    @Getter
    @Setter
    private BigDecimal price;

    @Getter
    @Setter
    private MotherAccountInfoModel account;
}
