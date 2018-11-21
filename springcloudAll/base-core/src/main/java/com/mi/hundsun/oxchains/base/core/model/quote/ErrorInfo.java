package com.mi.hundsun.oxchains.base.core.model.quote;

import java.io.Serializable;

/**
 * Created by xmf
 * Date: 2018/3/28
 **/
public class ErrorInfo implements Serializable {
    private static final long serialVersionUID = 476617419056937115L;
    /**
     * 错误码
     */
    private String error_no;
    /**
     * 错误信息
     */
    private String error_info;

    public String getError_no() {
        return error_no;
    }

    public void setError_no(String error_no) {
        this.error_no = error_no;
    }

    public String getError_info() {
        return error_info;
    }

    public void setError_info(String error_info) {
        this.error_info = error_info;
    }
}
