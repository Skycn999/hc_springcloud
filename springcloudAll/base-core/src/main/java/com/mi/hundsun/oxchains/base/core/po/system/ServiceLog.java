package com.mi.hundsun.oxchains.base.core.po.system;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.util.function.Consumer;


/**
 * 接口服务调用日志实体信息<br>
 *
 * @author bin
 * @date   2018-04-11 05:13:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "接口服务调用日志")
@Table(name = "s_service_log")
public class ServiceLog extends GenericPo<Integer> {

	public static final String TABLE_NAME = "s_service_log";


	/**操作IP地址**/
	@ApiModelProperty(value = "操作IP地址")
	private String ip;
	/**URL**/
	@ApiModelProperty(value = "URL")
	private String uri;
	/**操作方式 get or post**/
	@ApiModelProperty(value = "操作方式 get or post")
	private String method;
	/**操作结果**/
	@ApiModelProperty(value = "操作结果")
	private String code;
	/**操作提交的数据**/
	@ApiModelProperty(value = "操作提交的数据")
	private Object params;
	/**异常信息**/
	@ApiModelProperty(value = "异常信息")
	private Object exception;

	public ServiceLog(){}

	public ServiceLog(Consumer<ServiceLog> consumer){
		consumer.accept(this);
	}
}

