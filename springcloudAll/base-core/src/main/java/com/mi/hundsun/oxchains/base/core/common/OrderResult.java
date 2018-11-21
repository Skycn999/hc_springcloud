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
 * @date 2018-05-06 19:15.
 */
public class OrderResult implements Serializable {

    @Getter
    @Setter
    private String symbol;

    @Getter
    @Setter
    private String entrust_no;

    @Getter
    @Setter
    private String entrust_price;

    @Getter
    @Setter
    private String entrust_amount;

    @Getter
    @Setter
    private String entrust_bs;

    @Getter
    @Setter
    private String entrust_status;

    @Getter
    @Setter
    private String entrust_prop;

    @Getter
    @Setter
    private BusinessDetail businessDetail;
}
