package com.mi.hundsun.oxchains.base.core.model.quote;

import com.mi.hundsun.oxchains.base.core.model.quote.utils.CommonMethod;

import java.io.Serializable;

/***
 * K线数据
 * Created by xmf
 * Date: 2018/3/7
 **/
public class Kline implements Serializable, Comparable<Kline> {
    private static final long serialVersionUID = 8894640385947202971L;
    /**
     * 时间戳
     */
    private String timestamp;
    /**
     * 成交量
     */
    private String business_amount;
    /**
     * 成交笔数
     */
    private Integer business_count;
    /**
     * 开盘价
     */
    private String open_price;
    /**
     * 收盘价
     */
    private String close_price;
    /**
     * 最低价
     */
    private String low_price;
    /**
     * 最高价
     */
    private String high_price;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = CommonMethod.formNullStr(timestamp);
    }

    public String getBusiness_amount() {
        return business_amount;
    }

    public void setBusiness_amount(String business_amount) {
        this.business_amount = CommonMethod.formPriceAmount(business_amount);
    }

    public Integer getBusiness_count() {
        return business_count;
    }

    public void setBusiness_count(Integer business_count) {
        this.business_count = business_count;
    }

    public String getOpen_price() {
        return open_price;
    }

    public void setOpen_price(String open_price) {
        this.open_price = CommonMethod.formPriceAmount(open_price);
    }

    public String getClose_price() {
        return close_price;
    }

    public void setClose_price(String close_price) {
        this.close_price = CommonMethod.formPriceAmount(close_price);

    }

    public String getLow_price() {
        return low_price;
    }

    public void setLow_price(String low_price) {
        this.low_price = CommonMethod.formPriceAmount(low_price);
    }

    public String getHigh_price() {
        return high_price;
    }

    public void setHigh_price(String high_price) {
        this.high_price = CommonMethod.formPriceAmount(high_price);
    }

    @Override
    public String toString() {
        return "{" +
                "timestamp='" + timestamp + '\'' +
                ", business_amount=" + business_amount +
                ", business_count=" + business_count +
                ", open_price=" + open_price +
                ", close_price=" + close_price +
                ", low_price=" + low_price +
                ", high_price=" + high_price +
                '}';
    }

    @Override
    public int compareTo(Kline o) {
        String thisStrTime = this.getTimestamp();
        String thatStrTime = o.getTimestamp();
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

    /**
     * string成员变量对比
     *
     * @param that
     * @return
     */
    private boolean stringMemberEquals(Kline that) {
        if (this.timestamp.equals(that.timestamp) && this.business_amount.equals(that.business_amount) && this.high_price.equals(that.high_price) && this.low_price.equals(that.low_price) && this.close_price.equals(that.close_price) && this.open_price.equals(that.open_price)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Kline) {
            Kline that = (Kline) obj;
            if (null != this.business_count && null != that.business_count) {
                if (this.business_count.equals(that.business_count) && stringMemberEquals(that)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (null == this.business_count && null == that.business_count && stringMemberEquals(that)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
