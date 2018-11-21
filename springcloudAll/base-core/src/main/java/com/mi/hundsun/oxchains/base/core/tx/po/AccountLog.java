package com.mi.hundsun.oxchains.base.core.tx.po;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


/**
 * 资产变更记录表实体信息<br>
 *
 * @author fengting
 * @date   2018-04-13 09:54:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "资产变更记录表")
@Table(name = "tx_account_log")
public class AccountLog extends GenericPo<Integer> {
    public static final String TABLE_NAME = "tx_account_log";

    /**用户ID**/
    @ApiModelProperty(value = "用户ID")
    private Integer userId;
    /**操作的资产代码 如btc**/
    @ApiModelProperty(value = "操作的资产代码 如btc")
    private String optCoinCode;
    /**操作数量**/
    @ApiModelProperty(value = "操作数量")
    private BigDecimal optAmount;
    /**操作前总数量**/
    @ApiModelProperty(value = "操作前总数量")
    private BigDecimal beforeOptAmount;
    /**操作后总数量**/
    @ApiModelProperty(value = "操作后总数量")
    private BigDecimal afterOptAmount;
    /**使用后数量**/
    @ApiModelProperty(value = "冻结数量")
    private BigDecimal freezeAmount;
    /**途径 1,充币:recharge;2,提币:mention;3,扣除:deduct;4,冻结:freeze;5,手续费:serviceFee;6,解冻:unfreeze;7,提币冻结:mention_freeze;8,提币解冻:mention_unfreeze**/
    @ApiModelProperty(value = "途径 1,充币:recharge;2,提币:mention;3,扣除:deduct;4,交易冻结:freeze;5,手续费:serviceFee;6,解冻:unfreeze;7,提币冻结:mention_freeze;8,提币解冻:mention_unfreeze")
    private Integer approach;
    @Transient
    private String approachFormatter ;
    /**变更描述 如使用1个BTC换取100个EOS**/
    @ApiModelProperty(value = "变更描述 如使用1个BTC换取100个EOS")
    private String info;
    public String getApproachFormatter() {
        if(null == approachFormatter || "".equals(approachFormatter)){
            return APPROACH.getValue(getApproach());
        }
        return this.approachFormatter;
    }
    public void setApproachFormatter(String approachFormatter) {
        this.approachFormatter=approachFormatter;
    }

    /**1,充币:recharge<br>2,提币:mention<br>3,扣除:deduct<br>4,冻结:freeze<br>5,手续费:serviceFee<br>6,解冻:unfreeze<br>7,提币冻结:mention_freeze<br>8,提币解冻:mention_unfreeze**/
    public enum APPROACH {

        /**1,充币:recharge**/
        RECHARGE("充币",1),

        /**2,提币:mention**/
        MENTION("提币",2),

        /**3,扣除:deduct**/
        DEDUCT("扣除",3),

        /**4,交易冻结:freeze**/
        FREEZE("交易冻结",4),

        /**5,手续费:serviceFee**/
        SERVICEFEE("手续费",5),

        /**6,交易解冻:unfreeze**/
        UNFREEZE("交易解冻",6),

        /**7,提币冻结:mention_freeze**/
        MENTION_FREEZE("提币冻结",7),

        /**8,提币解冻:mention_unfreeze**/
        MENTION_UNFREEZE("提币解冻",8),

        /**8,交易获得:tx_get**/
        TX_GET("交易获得",9);

        public final int code;
        public final String value;
        private static Map<Integer, String> map = new HashMap<>();

        APPROACH(String value, int code) {
            this.code = code;
            this.value = value;
        }

        public static String getValue(Integer code) {
            if (null == code) {
                return null;
            }
            for (APPROACH approach : APPROACH.values()) {
                if (approach.code == code) {
                    return approach.value;
                }
            }
            return null;
        }

        public static Integer getCode(String value) {
            if (null == value  || "".equals(value)) {
                return null;
            }
            for (APPROACH approach : APPROACH.values()) {
                if (approach.value.equals(value)) {
                    return approach.code;
                }
            }
            return null;
        }

        public static  Map<Integer, String> getEnumMap() {
            if(map.size() == 0){
                for (APPROACH approach : APPROACH.values()) {
                    map.put(approach.code,approach.value);
                }
            }
            return map;
        }
    }

    public AccountLog(){}

    public AccountLog(Consumer<AccountLog> consumer){
    consumer.accept(this);
    }
}

