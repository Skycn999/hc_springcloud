package com.mi.hundsun.oxchains.base.core.po.exchange;

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
import  java.math.BigDecimal;


/**
 * 交易所母账号资产信息实体信息<br>
 *
 * @author bin
 * @date   2018-04-24 11:31:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "交易所母账号资产信息")
@Table(name = "ex_mother_account_info")
public class MotherAccountInfo extends GenericPo<Integer> {

    public static final String TABLE_NAME = "ex_mother_account_info";


    /**母账号编号**/
    @ApiModelProperty(value = "母账号编号")
    private String accountNo;
    /**交易所名称**/
    @ApiModelProperty(value = "交易所编号")
    private String exNo;
	/**母账号ID**/
	@ApiModelProperty(value = "母账号ID")
	private Integer motherAccountId;
	/**币种**/
	@ApiModelProperty(value = "币种")
	private String coinCurrency;
	/**剩余资产**/
	@ApiModelProperty(value = "剩余资产")
	private BigDecimal available;
	/**冻结资产**/
	@ApiModelProperty(value = "冻结资产")
	private BigDecimal freeze;
	/**总资产**/
	@ApiModelProperty(value = "总资产")
	private BigDecimal total;
	/**同步时间**/
	@ApiModelProperty(value = "同步时间")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
	private Date syncTime;

    public MotherAccountInfo(){}

    public MotherAccountInfo(Consumer<MotherAccountInfo> consumer){
    consumer.accept(this);
    }
}

