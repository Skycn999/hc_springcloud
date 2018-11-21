package com.mi.hundsun.oxchains.base.core.model.quote;

import com.mi.hundsun.oxchains.base.core.model.quote.utils.CommonMethod;

import java.io.Serializable;

/***
 * 聚合10档行情
 * copy by fengting
 * Date: 2018/4/15
 */
public class DepthVolume implements Serializable {
    private static final long serialVersionUID = 7574079257952434045L;
    /**
     * 卖1价
     */
    private String ask_price1;
    /**
     * 卖1量
     */
    private String ask_volume1;
    /**
     * 卖2价
     */
    private String ask_price2;
    /**
     * 卖2量
     */
    private String ask_volume2;
    /**
     * 卖3价
     */
    private String ask_price3;
    /**
     * 卖3量
     */
    private String ask_volume3;
    /**
     * 卖4价
     */
    private String ask_price4;
    /**
     * 卖4量
     */
    private String ask_volume4;
    /**
     * 卖5价
     */
    private String ask_price5;
    /**
     * 卖5量
     */
    private String ask_volume5;

    /**
     * 卖6价
     */
    private String ask_price6;
    /**
     * 卖6量
     */
    private String ask_volume6;
    /**
     * 卖7价
     */
    private String ask_price7;
    /**
     * 卖7量
     */
    private String ask_volume7;
    /**
     * 卖8价
     */
    private String ask_price8;
    /**
     * 卖8量
     */
    private String ask_volume8;
    /**
     * 卖9价
     */
    private String ask_price9;
    /**
     * 卖9量
     */
    private String ask_volume9;
    /**
     * 卖10价
     */
    private String ask_price10;
    /**
     * 卖10量
     */
    private String ask_volume10;

    /**
     * 买1价
     */
    private String bid_price1;
    /**
     * 买1量
     */
    private String bid_volume1;
    /**
     * 买2价
     */
    private String bid_price2;
    /**
     * 买2量
     */
    private String bid_volume2;
    /**
     * 买3价
     */
    private String bid_price3;
    /**
     * 买3量
     */
    private String bid_volume3;
    /**
     * 买4价
     */
    private String bid_price4;
    /**
     * 买4量
     */
    private String bid_volume4;
    /**
     * 买5价
     */
    private String bid_price5;
    /**
     * 买5量
     */
    private String bid_volume5;

    /**
     * 买6价
     */
    private String bid_price6;
    /**
     * 买6量
     */
    private String bid_volume6;
    /**
     * 买7价
     */
    private String bid_price7;
    /**
     * 买7量
     */
    private String bid_volume7;
    /**
     * 买8价
     */
    private String bid_price8;
    /**
     * 买8量
     */
    private String bid_volume8;
    /**
     * 买9价
     */
    private String bid_price9;
    /**
     * 买9量
     */
    private String bid_volume9;
    /**
     * 买10价
     */
    private String bid_price10;
    /**
     * 买10量
     */
    private String bid_volume10;

    public String getAsk_price1() {
        return ask_price1;
    }

    public void setAsk_price1(String ask_price1) {
        this.ask_price1 = CommonMethod.formPriceAmount(ask_price1);
    }

    public String getAsk_volume1() {
        return ask_volume1;
    }

    public void setAsk_volume1(String ask_volume1) {
        this.ask_volume1 = CommonMethod.formPriceAmount(ask_volume1);
    }

    public String getAsk_price2() {
        return ask_price2;
    }

    public void setAsk_price2(String ask_price2) {
        this.ask_price2 = CommonMethod.formPriceAmount(ask_price2);
    }

    public String getAsk_volume2() {
        return ask_volume2;
    }

    public void setAsk_volume2(String ask_volume2) {
        this.ask_volume2 = CommonMethod.formPriceAmount(ask_volume2);
    }

    public String getAsk_price3() {
        return ask_price3;
    }

    public void setAsk_price3(String ask_price3) {
        this.ask_price3 = CommonMethod.formPriceAmount(ask_price3);
    }

    public String getAsk_volume3() {
        return ask_volume3;
    }

    public void setAsk_volume3(String ask_volume3) {
        this.ask_volume3 = CommonMethod.formPriceAmount(ask_volume3);
    }

    public String getAsk_price4() {
        return ask_price4;
    }

    public void setAsk_price4(String ask_price4) {
        this.ask_price4 = CommonMethod.formPriceAmount(ask_price4);
    }

    public String getAsk_volume4() {
        return ask_volume4;
    }

    public void setAsk_volume4(String ask_volume4) {
        this.ask_volume4 = CommonMethod.formPriceAmount(ask_volume4);
    }

    public String getAsk_price5() {
        return ask_price5;
    }

    public void setAsk_price5(String ask_price5) {
        this.ask_price5 = CommonMethod.formPriceAmount(ask_price5);
    }

    public String getAsk_volume5() {
        return ask_volume5;
    }

    public void setAsk_volume5(String ask_volume5) {
        this.ask_volume5 = CommonMethod.formPriceAmount(ask_volume5);
    }

    public String getAsk_price6() {
        return ask_price6;
    }

    public void setAsk_price6(String ask_price6) {
        this.ask_price6 = CommonMethod.formPriceAmount(ask_price6);
    }

    public String getAsk_volume6() {
        return ask_volume6;
    }

    public void setAsk_volume6(String ask_volume6) {
        this.ask_volume6 = CommonMethod.formPriceAmount(ask_volume6);
    }

    public String getAsk_price7() {
        return ask_price7;
    }

    public void setAsk_price7(String ask_price7) {
        this.ask_price7 = CommonMethod.formPriceAmount(ask_price7);
    }

    public String getAsk_volume7() {
        return ask_volume7;
    }

    public void setAsk_volume7(String ask_volume7) {
        this.ask_volume7 = CommonMethod.formPriceAmount(ask_volume7);
    }

    public String getAsk_price8() {
        return ask_price8;
    }

    public void setAsk_price8(String ask_price8) {
        this.ask_price8 = CommonMethod.formPriceAmount(ask_price8);
    }

    public String getAsk_volume8() {
        return ask_volume8;
    }

    public void setAsk_volume8(String ask_volume8) {
        this.ask_volume8 = CommonMethod.formPriceAmount(ask_volume8);
    }

    public String getAsk_price9() {
        return ask_price9;
    }

    public void setAsk_price9(String ask_price9) {
        this.ask_price9 = CommonMethod.formPriceAmount(ask_price9);
    }

    public String getAsk_volume9() {
        return ask_volume9;
    }

    public void setAsk_volume9(String ask_volume9) {
        this.ask_volume9 = CommonMethod.formPriceAmount(ask_volume9);
    }

    public String getAsk_price10() {
        return ask_price10;
    }

    public void setAsk_price10(String ask_price10) {
        this.ask_price10 = CommonMethod.formPriceAmount(ask_price10);
    }

    public String getAsk_volume10() {
        return ask_volume10;
    }

    public void setAsk_volume10(String ask_volume10) {
        this.ask_volume10 = CommonMethod.formPriceAmount(ask_volume10);
    }

    public String getBid_price1() {
        return bid_price1;
    }

    public void setBid_price1(String bid_price1) {
        this.bid_price1 = CommonMethod.formPriceAmount(bid_price1);
    }

    public String getBid_volume1() {
        return bid_volume1;
    }

    public void setBid_volume1(String bid_volume1) {
        this.bid_volume1 = CommonMethod.formPriceAmount(bid_volume1);
    }

    public String getBid_price2() {
        return bid_price2;
    }

    public void setBid_price2(String bid_price2) {
        this.bid_price2 = CommonMethod.formPriceAmount(bid_price2);
    }

    public String getBid_volume2() {
        return bid_volume2;
    }

    public void setBid_volume2(String bid_volume2) {
        this.bid_volume2 = CommonMethod.formPriceAmount(bid_volume2);
    }

    public String getBid_price3() {
        return bid_price3;
    }

    public void setBid_price3(String bid_price3) {
        this.bid_price3 = CommonMethod.formPriceAmount(bid_price3);
    }

    public String getBid_volume3() {
        return bid_volume3;
    }

    public void setBid_volume3(String bid_volume3) {
        this.bid_volume3 = CommonMethod.formPriceAmount(bid_volume3);
    }

    public String getBid_price4() {
        return bid_price4;
    }

    public void setBid_price4(String bid_price4) {
        this.bid_price4 = CommonMethod.formPriceAmount(bid_price4);
    }

    public String getBid_volume4() {
        return bid_volume4;
    }

    public void setBid_volume4(String bid_volume4) {
        this.bid_volume4 = CommonMethod.formPriceAmount(bid_volume4);
    }

    public String getBid_price5() {
        return bid_price5;
    }

    public void setBid_price5(String bid_price5) {
        this.bid_price5 = CommonMethod.formPriceAmount(bid_price5);
    }

    public String getBid_volume5() {
        return bid_volume5;
    }

    public void setBid_volume5(String bid_volume5) {
        this.bid_volume5 = CommonMethod.formPriceAmount(bid_volume5);
    }

    public String getBid_price6() {
        return bid_price6;
    }

    public void setBid_price6(String bid_price6) {
        this.bid_price6 = CommonMethod.formPriceAmount(bid_price6);
    }

    public String getBid_volume6() {
        return bid_volume6;
    }

    public void setBid_volume6(String bid_volume6) {
        this.bid_volume6 = CommonMethod.formPriceAmount(bid_volume6);
    }

    public String getBid_price7() {
        return bid_price7;
    }

    public void setBid_price7(String bid_price7) {
        this.bid_price7 = CommonMethod.formPriceAmount(bid_price7);
    }

    public String getBid_volume7() {
        return bid_volume7;
    }

    public void setBid_volume7(String bid_volume7) {
        this.bid_volume7 = CommonMethod.formPriceAmount(bid_volume7);
    }

    public String getBid_price8() {
        return bid_price8;
    }

    public void setBid_price8(String bid_price8) {
        this.bid_price8 = CommonMethod.formPriceAmount(bid_price8);
    }

    public String getBid_volume8() {
        return bid_volume8;
    }

    public void setBid_volume8(String bid_volume8) {
        this.bid_volume8 = CommonMethod.formPriceAmount(bid_volume8);
    }

    public String getBid_price9() {
        return bid_price9;
    }

    public void setBid_price9(String bid_price9) {
        this.bid_price9 = CommonMethod.formPriceAmount(bid_price9);
    }

    public String getBid_volume9() {
        return bid_volume9;
    }

    public void setBid_volume9(String bid_volume9) {
        this.bid_volume9 = CommonMethod.formPriceAmount(bid_volume9);
    }

    public String getBid_price10() {
        return bid_price10;
    }

    public void setBid_price10(String bid_price10) {
        this.bid_price10 = CommonMethod.formPriceAmount(bid_price10);
    }

    public String getBid_volume10() {
        return bid_volume10;
    }

    public void setBid_volume10(String bid_volume10) {
        this.bid_volume10 = CommonMethod.formPriceAmount(bid_volume10);
    }
}
