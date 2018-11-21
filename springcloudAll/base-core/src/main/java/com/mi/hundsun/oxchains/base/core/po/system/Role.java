package com.mi.hundsun.oxchains.base.core.po.system;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.util.function.Consumer;


/**
 * 系统角色表实体信息<br>
 *
 * @author donfy
 * @date   2017-08-21 09:50:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "系统角色表")
@Table(name = "s_role")
public class Role extends GenericPo<Integer> {

    public static final String TABLE_NAME = "s_role";

	
	/**名称**/
	@ApiModelProperty(value = "名称")
	private String name;
	/**描述**/
	@ApiModelProperty(value = "描述")
	private String description;
	/**备注**/
	@ApiModelProperty(value = "备注")
	private String remark;

    public Role(){}

    public Role(Consumer<Role> consumer){
    consumer.accept(this);
    }
}

