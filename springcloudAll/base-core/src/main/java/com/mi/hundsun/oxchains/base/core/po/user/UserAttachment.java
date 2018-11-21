package com.mi.hundsun.oxchains.base.core.po.user;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.util.function.Consumer;


/**
 * 用户附属信息表实体信息<br>
 *
 * @author lzj
 * @date   2018-04-14 10:30:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户附属信息表")
@Table(name = "user_attachment")
public class UserAttachment extends GenericPo<Integer> {

    public static final String TABLE_NAME = "user_attachment";

	
	/**文件类型 如jpg,doc,jpeg等**/
	@ApiModelProperty(value = "文件类型 如jpg,doc,jpeg等")
	private String fileType;
	/**对应的真实地址**/
	@ApiModelProperty(value = "对应的真实地址")
	private String realPath;
	/**备注**/
	@ApiModelProperty(value = "备注")
	private String remark;

    public UserAttachment(){}

    public UserAttachment(Consumer<UserAttachment> consumer){
    consumer.accept(this);
    }
}

