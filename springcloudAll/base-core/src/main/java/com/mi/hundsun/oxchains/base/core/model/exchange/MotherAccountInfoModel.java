/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.base.core.model.exchange;

import com.mi.hundsun.oxchains.base.core.po.exchange.Exchange;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccountInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author 枫亭
 * @description 母账号信息model
 * @date 2018-05-01 21:16.
 */
@Data
public class MotherAccountInfoModel extends MotherAccountInfo {

    private Exchange ex;

    private String exNo;

    private String accountName;

    private String accountNo;

    private String accountPwd;

    private String apiKey;

    private String apiSecret;

    private String accountId;

    private String googlePrivateKey;

    private String accountEmail;

    private String accountMobile;

    private BigDecimal available;

}
