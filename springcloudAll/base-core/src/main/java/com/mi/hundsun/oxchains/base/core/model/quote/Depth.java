package com.mi.hundsun.oxchains.base.core.model.quote;

import com.mi.hundsun.oxchains.base.core.model.quote.utils.CommonMethod;

import java.io.Serializable;

/***
 * 聚合10档行情
 * copy by fengting
 * Date: 2018/4/15
 */
public class Depth extends DepthVolume implements Serializable {
    private static final long serialVersionUID = 4787506780874636775L;


    /**
     * 交易所5档交易量明细，json串
     */
    private String volume_detail;

    public String getVolume_detail() {
        return volume_detail;
    }

    public void setVolume_detail(String volume_detail) {
        this.volume_detail = CommonMethod.formNullStr(volume_detail);
    }
}
