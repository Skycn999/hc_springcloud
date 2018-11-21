package com.mi.hundsun.oxchains.base.common.entity;

import lombok.Data;

/**
 * 通用返回结果对象
 */
@Data
public class ResultEntity<T> {
    public static final int SUCCESS = 200;
    public static final int LOADING = 300;
    public static final int FAIL = 400;
    //未注册
    public static final int NO_REGISTER = 2001;
    //手机未认证
    public static final int NO_MOBILE_AUTH = 201;
    //邮箱未认证
    public static final int NO_EMAIL_AUTH = 202;
    //实名未认证
    public static final int NO_REALNAME_AUTH = 203;
    //谷歌验证器未绑定
    public static final int NO_GOOGLE_BIND = 204;
    //提币密码未设置
    public static final int MENTION_COIN_NOT_SET = 205;

    private int code;
    private String message = "";
    private String url = "";
    private T data;
    private String uuid;

    public static ResultEntity success() {
        ResultEntity restResult = new ResultEntity();
        restResult.code = SUCCESS;
        restResult.message = "操作成功";
        return restResult;
    }

    public static ResultEntity success(String msg) {
        ResultEntity restResult = new ResultEntity();
        restResult.code = ResultEntity.SUCCESS;
        restResult.message = msg;
        return restResult;
    }

    public static ResultEntity success(String msg,Object data) {
        ResultEntity restResult = new ResultEntity();
        restResult.code = ResultEntity.SUCCESS;
        restResult.message = msg;
        restResult.data = data;
        return restResult;
    }

    public static ResultEntity fail() {
        ResultEntity restResult = new ResultEntity();
        restResult.code = FAIL;
        restResult.message = "操作失败";
        return restResult;
    }

    public static ResultEntity fail(String msg) {
        ResultEntity restResult = new ResultEntity();
        restResult.code = FAIL;
        restResult.message = msg;
        return restResult;
    }


}
