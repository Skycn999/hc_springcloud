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
 * 收益汇总实体信息<br>
 *
 * @author bin
 * @date   2018-04-24 10:49:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "收益汇总")
@Table(name = "count_earnings")
public class CountEarnings extends GenericPo<Integer> {

    public static final String TABLE_NAME = "count_earnings";

	
	/**统计时间点**/
	@ApiModelProperty(value = "统计时间点")
	@JSONField(format="yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd")
	private Date countTimePoint;
	/**提币笔数**/
	@ApiModelProperty(value = "提币笔数")
	private Integer totalMentionCoin;
	/**提币手续费**/
	@ApiModelProperty(value = "提币手续费")
	private BigDecimal serviceFeeMention;
	/**交易手续费**/
	@ApiModelProperty(value = "交易手续费")
	private BigDecimal serviceFeeTx;
	/**交易额**/
	@ApiModelProperty(value = "交易额")
	private BigDecimal amountTx;

    public CountEarnings(){}

    public CountEarnings(Consumer<CountEarnings> consumer){
    consumer.accept(this);
    }
}

