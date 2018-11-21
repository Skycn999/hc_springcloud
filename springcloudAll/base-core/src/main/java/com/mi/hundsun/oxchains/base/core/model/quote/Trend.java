package com.mi.hundsun.oxchains.base.core.model.quote;

import com.mi.hundsun.oxchains.base.core.model.quote.utils.CommonMethod;

import java.io.Serializable;

/***
 * Created by xmf
 * Date: 2018/3/6
 **/
public class Trend implements Serializable, Comparable<Trend> {
    private static final long serialVersionUID = -36428384472446574L;
    /**
     * 最新价
     */
    private String last_px;
    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 成交量
     */
    private String business_amount;

    public String getLast_px() {
        return last_px;
    }

    public void setLast_px(String last_px) {
        this.last_px = CommonMethod.formPriceAmount(last_px);
    }

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

    @Override
    public String toString() {
        return "{" +
                "last_px=" + last_px +
                ", timestamp='" + timestamp + '\'' +
                ", business_amount=" + business_amount +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Trend) {
            Trend that = (Trend) obj;
            if (this.timestamp.equals(that.timestamp) && this.last_px.equals(that.last_px)) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int compareTo(Trend o) {
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
}
