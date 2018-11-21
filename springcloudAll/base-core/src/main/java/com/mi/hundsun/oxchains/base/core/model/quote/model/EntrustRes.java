package com.mi.hundsun.oxchains.base.core.model.quote.model;


import com.mi.hundsun.oxchains.base.core.model.quote.utils.CommonMethod;

import java.io.Serializable;

/**
 * Created by xmf
 * Date: 2018/4/23
 **/
public class EntrustRes implements Serializable {
    private static final long serialVersionUID = 1462917776253094452L;
    private String entrust_no = "";

    public String getEntrust_no() {
        return entrust_no;
    }

    public void setEntrust_no(String entrust_no) {
        this.entrust_no = CommonMethod.formNullStr(entrust_no);
    }

    @Override
    public String toString() {
        return "EntrustRes{" +
                "entrust_no='" + entrust_no + '\'' +
                '}';
    }
}
