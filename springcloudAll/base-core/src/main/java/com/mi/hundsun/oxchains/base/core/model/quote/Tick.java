package com.mi.hundsun.oxchains.base.core.model.quote;

import com.mi.hundsun.oxchains.base.core.model.quote.utils.CommonMethod;

import java.io.Serializable;

/***
 * 分笔行情数据
 * Created by xmf
 * Date: 2018/3/13
 **/
public class Tick implements Serializable, Comparable<Tick> {
    private static final long serialVersionUID = 5038509954315155881L;

    /**
     * 成交价
     */
    private String business_price;
    /**
     * 成交量
     */
    private String business_amount;
    /**
     * 成交方向
     */
    private String business_direction;
    /**
     * 成交时间
     */
    private String business_time;

    public String getBusiness_price() {
        return business_price;
    }

    public void setBusiness_price(String business_price) {
        this.business_price = CommonMethod.formPriceAmount(business_price);
    }

    public String getBusiness_amount() {
        return business_amount;
    }

    public void setBusiness_amount(String business_amount) {
        this.business_amount = CommonMethod.formPriceAmount(business_amount);
    }

    public String getBusiness_direction() {
        return business_direction;
    }

    public void setBusiness_direction(String business_direction) {
        this.business_direction = CommonMethod.formNullStr(business_direction);
    }

    public String getBusiness_time() {
        return business_time;
    }

    public void setBusiness_time(String business_time) {
        this.business_time = CommonMethod.formNullStr(business_time);
    }

    @Override
    public int compareTo(Tick o) {
        String thisStrTime = this.getBusiness_time();
        String thatStrTime = o.getBusiness_time();
        long thisTime = Long.parseLong(thisStrTime == null ? "0" : thisStrTime);
        long thatTime = Long.parseLong(thatStrTime == null ? "0" : thatStrTime);
        long gap = thisTime - thatTime;
        if (gap > 0) {
            return 1;
        } else if (gap < 0) {
            return -1;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tick) {
            Tick that = (Tick) obj;
            if (this.business_time.equals(that.business_time) && this.business_price.equals(that.business_price) && this.business_amount.equals(that.business_amount) && this.business_direction.equals(that.business_direction)) {
                return true;
            }
            return false;
        }
        return false;
    }
}
