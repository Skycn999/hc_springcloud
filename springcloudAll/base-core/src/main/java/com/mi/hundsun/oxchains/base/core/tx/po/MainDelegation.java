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
 * 交易管理-主委托管理实体信息<br>
 *
 * @author fengting
 * @date   2018-04-13 09:54:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "交易管理-主委托管理")
@Table(name = "tx_main_delegation")
public class MainDelegation extends GenericPo<Integer> {

    public static final String TABLE_NAME = "tx_main_delegation";

	
	/**用户ID**/
	@ApiModelProperty(value = "用户ID")
	private Integer userId;
	/**委托编号**/
	@ApiModelProperty(value = "委托编号")
	private String delegateNo;
	/**委托方式 1,市价委托:market;2,限价委托:limited**/
	@ApiModelProperty(value = "委托方式 1,市价委托:market;2,限价委托:limited")
	private Integer style;
    @Transient
    private String styleFormatter ;
	/**来源 1,PC:pc;2,IOS:ios;3,安卓:andriod**/
	@ApiModelProperty(value = "来源 1,PC:pc;2,IOS:ios;3,安卓:andriod")
	private Integer origin;
    @Transient
    private String originFormatter ;
	/**方向 1,买入:buyIn;2,卖出:sellOut**/
	@ApiModelProperty(value = "方向 1,买入:buyIn;2,卖出:sellOut")
	private Integer direction;
    @Transient
    private String directionFormatter ;
	/**委托币种**/
	@ApiModelProperty(value = "委托币种")
	private String coinCurrency;
	/**委托资产代码**/
	@ApiModelProperty(value = "委托资产代码")
	private String coinCode;
	/**委托量**/
	@ApiModelProperty(value = "委托量")
	private BigDecimal amount;
	/**委托价**/
	@ApiModelProperty(value = "委托价")
	private BigDecimal price;
	/**交易额**/
	@ApiModelProperty(value = "交易额")
	private BigDecimal gmv;
	/**手续费率**/
	@ApiModelProperty(value = "手续费率")
	private BigDecimal serviceFeeScale;
	/**状态 1,下单申请(已报):reported;2,委托中:commissionedIn;3,撤单中:revoking;4,已撤单:revoked;5,部分成交撤回:partOfDealAndRevoked;6,委托失败:failed;7,已成交:deal**/
	@ApiModelProperty(value = "状态 1,下单申请(已报):reported;2,委托中:commissionedIn;3,撤单中:revoking;4,已撤单:revoked;5,部分成交撤回:partOfDealAndRevoked;6,委托失败:failed;7,已成交:deal")
	private Integer state;
    /**备注**/
    @ApiModelProperty(value = "备注")
    private String remark;
    /**是否有兄弟订单 0否 1是**/
    @ApiModelProperty(value = "是否有兄弟订单 0否 1是")
    private Integer isSplit;
    @Transient
    private String exchangeNo ;
    @Transient
    private String stateFormatter ;
    public String getStyleFormatter() {
        if(null == styleFormatter || "".equals(styleFormatter)){
            return STYLE.getValue(getStyle());
        }
        return this.styleFormatter;
    }
    public void setStyleFormatter(String styleFormatter) {
        this.styleFormatter=styleFormatter;
    }
    public String getOriginFormatter() {
        if(null == originFormatter || "".equals(originFormatter)){
            return ORIGIN.getValue(getOrigin());
        }
        return this.originFormatter;
    }
    public void setOriginFormatter(String originFormatter) {
        this.originFormatter=originFormatter;
    }
    public String getDirectionFormatter() {
        if(null == directionFormatter || "".equals(directionFormatter)){
            return DIRECTION.getValue(getDirection());
        }
        return this.directionFormatter;
    }
    public void setDirectionFormatter(String directionFormatter) {
        this.directionFormatter=directionFormatter;
    }
    public String getStateFormatter() {
        if(null == stateFormatter || "".equals(stateFormatter)){
            return STATE.getValue(getState());
        }
        return this.stateFormatter;
    }

    /**1,市价委托:market<br>2,限价委托:limited**/
    public enum STYLE {

        /**1,市价委托:market**/
        MARKET("市价委托",1),

        /**2,限价委托:limited**/
        LIMITED("限价委托",2);

        public final int code;
        public final String value;
        private static Map<Integer, String> map = new HashMap<>();

        STYLE(String value, int code) {
            this.code = code;
            this.value = value;
        }

        public static String getValue(Integer code) {
            if (null == code) {
                return null;
            }
            for (STYLE style : STYLE.values()) {
                if (style.code == code) {
                    return style.value;
                }
            }
            return null;
        }

        public static Integer getCode(String value) {
            if (null == value  || "".equals(value)) {
                return null;
            }
            for (STYLE style : STYLE.values()) {
                if (style.value.equals(value)) {
                    return style.code;
                }
            }
            return null;
        }

        public static  Map<Integer, String> getEnumMap() {
            if(map.size() == 0){
                for (STYLE style : STYLE.values()) {
                    map.put(style.code,style.value);
                }
            }
            return map;
        }
    }

    /**1,PC:pc<br>2,IOS:ios<br>3,安卓:android**/
    public enum ORIGIN {

        /**1,PC:pc**/
        PC("PC",1),

        /**2,IOS:ios**/
        IOS("IOS",2),

        /**3,安卓:android**/
        ANDROID("安卓",3);

        public final int code;
        public final String value;
        private static Map<Integer, String> map = new HashMap<>();

        ORIGIN(String value, int code) {
            this.code = code;
            this.value = value;
        }

        public static String getValue(Integer code) {
            if (null == code) {
                return null;
            }
            for (ORIGIN origin : ORIGIN.values()) {
                if (origin.code == code) {
                    return origin.value;
                }
            }
            return null;
        }

        public static Integer getCode(String value) {
            if (null == value  || "".equals(value)) {
                return null;
            }
            for (ORIGIN origin : ORIGIN.values()) {
                if (origin.value.equals(value)) {
                    return origin.code;
                }
            }
            return null;
        }

        public static  Map<Integer, String> getEnumMap() {
            if(map.size() == 0){
                for (ORIGIN origin : ORIGIN.values()) {
                    map.put(origin.code,origin.value);
                }
            }
            return map;
        }
    }

    /**1,买入:buyIn<br>2,卖出:sellOut**/
    public enum DIRECTION {

        /**1,买入:buyIn**/
        BUYIN("买入",1),

        /**2,卖出:sellOut**/
        SELLOUT("卖出",2);

        public final int code;
        public final String value;
        private static Map<Integer, String> map = new HashMap<>();

        DIRECTION(String value, int code) {
            this.code = code;
            this.value = value;
        }

        public static String getValue(Integer code) {
            if (null == code) {
                return null;
            }
            for (DIRECTION direction : DIRECTION.values()) {
                if (direction.code == code) {
                    return direction.value;
                }
            }
            return null;
        }

        public static Integer getCode(String value) {
            if (null == value  || "".equals(value)) {
                return null;
            }
            for (DIRECTION direction : DIRECTION.values()) {
                if (direction.value.equals(value)) {
                    return direction.code;
                }
            }
            return null;
        }

        public static  Map<Integer, String> getEnumMap() {
            if(map.size() == 0){
                for (DIRECTION direction : DIRECTION.values()) {
                    map.put(direction.code,direction.value);
                }
            }
            return map;
        }
    }

    /**1,下单申请(已报):reported<br>2,委托中:commissionedIn<br>3,撤单中:revoking<br>4,已撤单:revoked<br>5,部分成交撤回:partOfDealAndRevoked<br>6,委托失败:failed<br>7,已成交:deal**/
    public enum STATE {

        /**1,下单申请(已报):reported**/
        REPORTED("下单申请(已报)",1),

        /**2,委托中:commissionedIn**/
        COMMISSIONED_IN("委托中",2),

        /**3,已完成:finished**/
        FINISHED("已完成",3),

        /**4,失败:failed**/
        FAILED("失败",4);

        public final int code;
        public final String value;
        private static Map<Integer, String> map = new HashMap<>();

        STATE(String value, int code) {
            this.code = code;
            this.value = value;
        }

        public static String getValue(Integer code) {
            if (null == code) {
                return null;
            }
            for (STATE state : STATE.values()) {
                if (state.code == code) {
                    return state.value;
                }
            }
            return null;
        }

        public static Integer getCode(String value) {
            if (null == value  || "".equals(value)) {
                return null;
            }
            for (STATE state : STATE.values()) {
                if (state.value.equals(value)) {
                    return state.code;
                }
            }
            return null;
        }

        public static  Map<Integer, String> getEnumMap() {
            if(map.size() == 0){
                for (STATE state : STATE.values()) {
                    map.put(state.code,state.value);
                }
            }
            return map;
        }
    }

    public MainDelegation(){}

    public MainDelegation(Consumer<MainDelegation> consumer){
    consumer.accept(this);
    }
}

