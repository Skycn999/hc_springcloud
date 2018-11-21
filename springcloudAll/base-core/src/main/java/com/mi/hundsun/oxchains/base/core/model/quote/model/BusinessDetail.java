package com.mi.hundsun.oxchains.base.core.model.quote.model;


import com.mi.hundsun.oxchains.base.core.model.quote.utils.CommonMethod;

import java.io.Serializable;

/**
 * Created by xmf
 * Date: 2018/4/23
 **/
public class BusinessDetail implements Serializable {
    private static final long serialVersionUID = -864713259359108399L;
    /**
     * 成交数量
     */
    private String business_amount;
    /**
     * 成交编号
     */
    private String business_no;
    /**
     * 成交价格
     */
    private String business_price;

    /**
     * 手续费
     */
    private String fee;

    public String getBusiness_amount() {
        return business_amount;
    }

    public void setBusiness_amount(String business_amount) {
        this.business_amount = CommonMethod.formPriceAmount(business_amount);
    }

    public String getBusiness_no() {
        return business_no;
    }

    public void setBusiness_no(String business_no) {
        this.business_no = business_no;
    }

    public String getBusiness_price() {
        return business_price;
    }

    public void setBusiness_price(String business_price) {
        this.business_price = CommonMethod.formPriceAmount(business_price);
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = CommonMethod.formPriceAmount(fee);
    }
}
