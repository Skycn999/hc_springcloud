package com.mi.hundsun.oxchains.base.core.po.system;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 第三方接口请求记录实体信息<br>
 *
 * @author bin
 * @date   2018-04-24 02:00:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "第三方接口请求记录")
@Table(name = "s_tpi_request_log")
public class TpiRequestLog extends GenericPo<Integer> {

    public static final String TABLE_NAME = "s_tpi_request_log";

	
	/**用户ID**/
	@ApiModelProperty(value = "用户ID")
	private Integer userId;
	/**接口提供方名称**/
	@ApiModelProperty(value = "接口提供方名称")
	private String interfaceProvideName;
	/**接口名称**/
	@ApiModelProperty(value = "接口名称")
	private String interfaceName;
	/**接口url**/
	@ApiModelProperty(value = "接口url")
	private String interfaceUrl;
	/**接口请求参数**/
	@ApiModelProperty(value = "接口请求参数")
	private Object requestParams;
	/**接口返回参数**/
	@ApiModelProperty(value = "接口返回参数")
	private Object responseParams;
	/**接口请求时间**/
	@ApiModelProperty(value = "接口请求时间")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
	private Date requestTime;
	/**审核备注**/
	@ApiModelProperty(value = "审核备注")
	private String remark;

    public TpiRequestLog(){}

    public TpiRequestLog(Consumer<TpiRequestLog> consumer){
    consumer.accept(this);
    }
}

