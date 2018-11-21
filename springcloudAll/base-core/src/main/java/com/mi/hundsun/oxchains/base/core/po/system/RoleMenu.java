package com.mi.hundsun.oxchains.base.core.po.system;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.util.function.Consumer;


/**
 * 操作权限实体信息<br>
 *
 * @author donfy
 * @date   2017-08-15 03:14:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "操作权限")
@Table(name = "s_role_menu")
public class RoleMenu extends GenericPo<Integer> {

    public static final String TABLE_NAME = "s_role_menu";

	
	/**角色ID**/
	@ApiModelProperty(value = "角色ID")
	private Integer roleId;
	/**菜单ID**/
	@ApiModelProperty(value = "菜单ID")
	private Integer menuId;

    public RoleMenu(){}

    public RoleMenu(Consumer<RoleMenu> consumer){
    consumer.accept(this);
    }
}

