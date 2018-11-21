package com.mi.hundsun.oxchains.base.core.po.count;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.function.Consumer;
import javax.persistence.Table;

import  java.math.BigDecimal;


/**
 * 用户持仓汇总实体信息<br>
 *
 * @author bin
 * @date   2018-04-24 10:49:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户持仓汇总")
@Table(name = "count_user_account")
public class CountUserAccount extends GenericPo<Integer> {

    public static final String TABLE_NAME = "count_user_account";

	
	/**交易所编号**/
	@ApiModelProperty(value = "交易所编号")
	private String exNo;
	/**交易所名称**/
	@ApiModelProperty(value = "交易所名称")
	private String exName;
	/**交易所母账号**/
	@ApiModelProperty(value = "交易所母账号")
	private String exMotherAccount;
	/**资产代码**/
	@ApiModelProperty(value = "资产代码")
	private String code;
	/**交易笔数**/
	@ApiModelProperty(value = "交易笔数")
	private BigDecimal total;
	/**交易量冻结数量**/
	@ApiModelProperty(value = "交易量冻结数量")
	private BigDecimal freeze;
	/**可用数量**/
	@ApiModelProperty(value = "可用数量")
	private BigDecimal availabe;

    public CountUserAccount(){}

    public CountUserAccount(Consumer<CountUserAccount> consumer){
    consumer.accept(this);
    }
}

