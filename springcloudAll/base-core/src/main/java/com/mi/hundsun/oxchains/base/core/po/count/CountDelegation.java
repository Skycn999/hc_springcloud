package com.mi.hundsun.oxchains.base.core.po.count;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.function.Consumer;
import javax.persistence.Table;

import  java.util.Date;

import  com.alibaba.fastjson.annotation.JSONField;
import  java.math.BigDecimal;


/**
 * 委托统计实体信息<br>
 *
 * @author bin
 * @date   2018-04-24 10:49:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "委托统计")
@Table(name = "count_delegation")
public class CountDelegation extends GenericPo<Integer> {

    public static final String TABLE_NAME = "count_delegation";

	
	/**交易所编号**/
	@ApiModelProperty(value = "交易所编号")
	private String exNo;
	/**交易所名称**/
	@ApiModelProperty(value = "交易所名称")
	private String name;
	/**统计时间点**/
	@ApiModelProperty(value = "统计时间点")
	@JSONField(format="yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd")
	private Date countTimePoint;
	/**总委托次数**/
	@ApiModelProperty(value = "总委托次数")
	private Integer totalDelegate;
	/**失败次数**/
	@ApiModelProperty(value = "失败次数")
	private Integer failureTimes;
	/**成功率**/
	@ApiModelProperty(value = "成功率")
	private BigDecimal successRate;
	/**失败率**/
	@ApiModelProperty(value = "失败率")
	private BigDecimal failureRate;

    public CountDelegation(){}

    public CountDelegation(Consumer<CountDelegation> consumer){
    consumer.accept(this);
    }
}

