package com.mi.hundsun.oxchains.base.core.po.system;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.function.Consumer;
import javax.persistence.Table;
import javax.persistence.Transient;

import  java.util.Map;
import  java.util.Date;
import  java.util.HashMap;
import  com.alibaba.fastjson.annotation.JSONField;


/**
 * 管理员与角色关联实体信息<br>
 *
 * @author bin
 * @date   2018-04-11 05:57:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "管理员与角色关联")
@Table(name = "s_admin_role")
public class AdminRole extends GenericPo<Integer> {

    public static final String TABLE_NAME = "s_admin_role";

	/**管理员ID**/
	@ApiModelProperty(value = "管理员ID")
	private Integer adminId;
	/**角色ID**/
	@ApiModelProperty(value = "角色ID")
	private Integer roleId;

    public AdminRole(){}

    public AdminRole(Consumer<AdminRole> consumer){
    consumer.accept(this);
    }
}

