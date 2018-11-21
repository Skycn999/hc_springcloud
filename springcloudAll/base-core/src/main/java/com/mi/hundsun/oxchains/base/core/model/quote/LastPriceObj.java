package com.mi.hundsun.oxchains.base.core.model.quote;

import com.mi.hundsun.oxchains.base.core.model.quote.utils.CommonMethod;

import java.io.Serializable;

/**
 * Created by xmf
 * Date: 2018/4/2
 **/
public class LastPriceObj implements Serializable {
    private static final long serialVersionUID = 7210466401652711044L;
    /**
     * 最新价
     */
    private String lastPrice;
    /**
     * 涨幅
     */
    private String priceIncrease;


    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = CommonMethod.formPriceAmount(lastPrice);
    }

    public String getPriceIncrease() {
        return priceIncrease;
    }

    public void setPriceIncrease(String priceIncrease) {
        this.priceIncrease = priceIncrease;
    }
}
