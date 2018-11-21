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
 * 交易汇总实体信息<br>
 *
 * @author bin
 * @date   2018-04-24 10:49:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "交易汇总")
@Table(name = "count_transaction")
public class CountTransaction extends GenericPo<Integer> {

    public static final String TABLE_NAME = "count_transaction";

	
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
	/**资产代码**/
	@ApiModelProperty(value = "资产代码")
	private String code;
	/**交易笔数**/
	@ApiModelProperty(value = "交易笔数")
	private Integer totalTransactions;
	/**交易量**/
	@ApiModelProperty(value = "交易量")
	private BigDecimal volumeTx;
	/**交易额**/
	@ApiModelProperty(value = "交易额")
	private BigDecimal amountTx;

    public CountTransaction(){}

    public CountTransaction(Consumer<CountTransaction> consumer){
    consumer.accept(this);
    }
}

