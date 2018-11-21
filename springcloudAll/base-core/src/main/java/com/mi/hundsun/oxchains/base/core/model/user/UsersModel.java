package com.mi.hundsun.oxchains.base.core.model.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UsersModel extends Users{

 private UserIdentify a;

 private String mark;

 /**
  * 注册用户数量
  */
 @ApiModelProperty(value = "注册用户数量")
 private Long userNum;

 /**
  * 注册时间
  */
 @ApiModelProperty(value = "注册时间")
 @JSONField(format="yyyy-MM-dd")
 private Date registerTime;

 /**
  * 登录用户数量
  */
 @ApiModelProperty(value = "登录用户数量")
 private Long LoginNum;

}
