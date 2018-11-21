/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.base.core.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author 枫亭
 * @date 2018-05-06 19:16.
 */
public class BusinessDetail implements Serializable {

    @Getter
    @Setter
    private String business_amount; //成交数量

    @Getter
    @Setter
    private String business_no; //成交编号

    @Getter
    @Setter
    private String business_price; //成交价格

}
