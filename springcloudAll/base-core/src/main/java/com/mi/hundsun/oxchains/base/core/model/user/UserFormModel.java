package com.mi.hundsun.oxchains.base.core.model.user;

import lombok.Data;

@Data
public class UserFormModel {

 private String mark;

 /**
  * 开始时间
  */
 private String startTime;

 /**
  * 结束时间
  */
 private String endTime;


 /**
  * 注册用户数量
  */
 private Long regNum;

 /**
  * 登录用户数量
  */
 private Long loginNum;

 private Long totalNum;

 private int onlineNum;
}
