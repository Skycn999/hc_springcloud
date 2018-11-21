package com.mi.hundsun.oxchains.base.core.model.quote.model;


import com.mi.hundsun.oxchains.base.core.model.quote.utils.CommonMethod;

import java.io.Serializable;

/**
 * Created by xmf
 * Date: 2018/4/23
 **/
public class AccountBalance implements Serializable, Comparable<AccountBalance> {
    private static final long serialVersionUID = 4947387063090355007L;
    /**
     * 币种
     */
    private String currency_code;
    /**
     * 可用余额
     */
    private String enable_balance;
    /**
     * 冻结余额
     */
    private String frozen_balance;

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getEnable_balance() {
        return enable_balance;
    }

    public void setEnable_balance(String enable_balance) {
        this.enable_balance = CommonMethod.formPriceAmount(enable_balance);
    }

    public String getFrozen_balance() {
        return frozen_balance;
    }

    public void setFrozen_balance(String frozen_balance) {
        this.frozen_balance = CommonMethod.formPriceAmount(frozen_balance);
    }

    @Override
    public String toString() {
        return "AccountBalance{" +
                "currentcy_code='" + currency_code + '\'' +
                ", enable_balance='" + enable_balance + '\'' +
                ", frozen_balance='" + frozen_balance + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int compareTo(AccountBalance o) {
        int result = 1;
        if (null != o) {
            if (this.currency_code.compareTo(o.currency_code) == 0) {
                result = 0;
            } else if (this.currency_code.compareTo(o.currency_code) < 0) {
                return -1;
            }
        }
        return result;
    }
}
