package com.mi.hundsun.oxchains.base.core.model.user;

import lombok.Data;

@Data
public class UserCountModel {
    /**
     * 用户注册数量
     */
    private Long userRegisterCount;

    /**
     * 用户登录数量
     */
    private Long userLoginCount;

    /**
     * 时间点
     */
    private String timePoint;
}
