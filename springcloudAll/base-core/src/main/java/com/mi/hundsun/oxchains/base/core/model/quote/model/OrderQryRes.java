package com.mi.hundsun.oxchains.base.core.model.quote.model;


import com.mi.hundsun.oxchains.base.core.model.quote.utils.CommonMethod;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xmf
 * Date: 2018/4/23
 **/
public class OrderQryRes implements Serializable {
    private static final long serialVersionUID = -887136424648258736L;
    /**
     * 交易对
     */
    private String symbol;
    /**
     * 委托编号
     */
    private String entrust_no;
    /**
     * 委托价格
     */
    private String entrust_price;
    /**
     * 委托数量
     */
    private String entrust_amount;
    /**
     * 委托方向
     */
    private String entrust_bs;
    /**
     * 委托状态
     */
    private String entrust_status;
    /**
     * 委托类型
     */
    private String entrust_prop;
    /**
     * 成交明细
     */
    private List<BusinessDetail> business_detail;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = CommonMethod.formNullStr(symbol);
    }

    public String getEntrust_no() {
        return entrust_no;
    }

    public void setEntrust_no(String entrust_no) {
        this.entrust_no = CommonMethod.formNullStr(entrust_no);
    }

    public String getEntrust_price() {
        return entrust_price;
    }

    public void setEntrust_price(String entrust_price) {
        this.entrust_price = CommonMethod.formPriceAmount(entrust_price);
    }

    public String getEntrust_amount() {
        return entrust_amount;
    }

    public void setEntrust_amount(String entrust_amount) {
        this.entrust_amount = CommonMethod.formPriceAmount(entrust_amount);
    }

    public String getEntrust_bs() {
        return entrust_bs;
    }

    public void setEntrust_bs(String entrust_bs) {
        this.entrust_bs = CommonMethod.formNullStr(entrust_bs);
    }

    public String getEntrust_status() {
        return entrust_status;
    }

    public void setEntrust_status(String entrust_status) {
        this.entrust_status = CommonMethod.formNullStr(entrust_status);
    }

    public String getEntrust_prop() {
        return entrust_prop;
    }

    public void setEntrust_prop(String entrust_prop) {
        this.entrust_prop = CommonMethod.formNullStr(entrust_prop);
    }

    public List<BusinessDetail> getBusiness_detail() {
        return business_detail;
    }

    public void setBusiness_detail(List<BusinessDetail> business_detail) {
        this.business_detail = business_detail;
    }

    @Override
    public String toString() {
        return "OrderQryRes{" +
                "symbol='" + symbol + '\'' +
                ", entrust_no='" + entrust_no + '\'' +
                ", entrust_price='" + entrust_price + '\'' +
                ", entrust_amount='" + entrust_amount + '\'' +
                ", entrust_bs='" + entrust_bs + '\'' +
                ", entrust_status='" + entrust_status + '\'' +
                ", entrust_prop='" + entrust_prop + '\'' +
                ", business_detail=" + business_detail +
                '}';
    }
}
