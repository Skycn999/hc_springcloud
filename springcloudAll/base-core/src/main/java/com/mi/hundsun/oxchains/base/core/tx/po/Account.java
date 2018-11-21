package com.mi.hundsun.oxchains.base.core.tx.po;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.function.Consumer;


/**
 * 用户资产持仓表实体信息<br>
 *
 * @author fengting
 * @date   2018-04-13 09:54:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户资产持仓表")
@Table(name = "tx_account")
public class Account extends GenericPo<Integer> {

    public static final String TABLE_NAME = "tx_account";

	
	/**用户ID**/
	@ApiModelProperty(value = "用户ID")
	private Integer userId;
	/**资产代码**/
	@ApiModelProperty(value = "资产代码")
	private String coinCode;
	/**交易所编号**/
	@ApiModelProperty(value = "交易所编号")
	private String exchangeNo;
	/**交易名称**/
	@ApiModelProperty(value = "交易名称")
	private String exchangeName;
	/**母账号ID**/
	@ApiModelProperty(value = "母账号ID")
	private Integer motherAccountId;
	/**母账号**/
	@ApiModelProperty(value = "母账号")
	private String motherAccount;
	/**总数量**/
	@ApiModelProperty(value = "总数量")
	private BigDecimal total;
	/**可用数量**/
	@ApiModelProperty(value = "可用数量")
	private BigDecimal available;
	/**冻结数量**/
	@ApiModelProperty(value = "冻结数量")
	private BigDecimal freeze;

    public Account(){}

    public Account(Consumer<Account> consumer){
    consumer.accept(this);
    }

    public static String getExName(String exchangeNo) {
        if(exchangeNo.equalsIgnoreCase("huobi")) {
            return "火币";
        } else if(exchangeNo.equalsIgnoreCase("bian")) {
            return "币安";
        } else if(exchangeNo.equalsIgnoreCase("bitfinex")) {
            return "BitFinex";
        } else if(exchangeNo.equalsIgnoreCase("okex")) {
            return "OKEX";
        }
        return "--";
    }
}

