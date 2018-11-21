package com.mi.hundsun.oxchains.base.core.model.quote.model;

import com.mi.hundsun.oxchains.base.core.model.quote.utils.CommonMethod;

import java.io.Serializable;

/**
 * Created by xmf
 * Date: 2018/4/26
 **/
public class WithDrawRes implements Serializable {
    private static final long serialVersionUID = 4475350554728717149L;
    /**
     * 委托编号
     */
    private String order_id;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = CommonMethod.formNullStr(order_id);
    }

    @Override
    public String toString() {
        return "WithDrawRes{" +
                "order_id='" + order_id + '\'' +
                '}';
    }
}
