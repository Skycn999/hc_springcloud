package com.mi.hundsun.oxchains.base.core.po.fn;import com.fasterxml.jackson.annotation.JsonFormat;import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;import lombok.Data;import lombok.EqualsAndHashCode;import io.swagger.annotations.ApiModel;import io.swagger.annotations.ApiModelProperty;import java.util.function.Consumer;import javax.persistence.Table;import javax.persistence.Transient;import  java.util.Map;import  java.util.Date;import  java.util.HashMap;import  com.alibaba.fastjson.annotation.JSONField;import  java.math.BigDecimal;/** * 资产划拨记录实体信息<br> * * @author bin * @date   2018-04-15 08:44:13 */@Data@EqualsAndHashCode(callSuper = true)@ApiModel(description = "资产划拨记录")@Table(name = "fn_plat_asset_log")public class PlatAssetLog extends GenericPo<Integer> {    public static final String TABLE_NAME = "fn_plat_asset_log";		/**流水号(第三方流水号)**/	@ApiModelProperty(value = "流水号(第三方流水号)")	private String billNo;	/**币种**/	@ApiModelProperty(value = "币种")	private String coinCurrency;	/**转换方向 1,平台到母账号:toaccount;2,母账号到平台:toplatform**/	@ApiModelProperty(value = "转换方向 1,平台到母账号:toaccount;2,母账号到平台:toplatform")	private Integer direction;	@Transient	private String directionFormatter ;	/**状态 0,正常:normal;1,作废:delete**/	@ApiModelProperty(value = "状态 0,正常:normal;1,作废:delete")	private Integer state;	@Transient	private String stateFormatter ;	/**交易所**/	@ApiModelProperty(value = "交易所")	private String exchange;	/**平台地址**/	@ApiModelProperty(value = "平台地址")	private String platAddr;	/**母账号**/	@ApiModelProperty(value = "母账号")	private String motherAccount;	/**划拨数量**/	@ApiModelProperty(value = "划拨数量")	private BigDecimal amount;	/**转币时间**/	@ApiModelProperty(value = "转币时间")	@JSONField(format="yyyy-MM-dd HH:mm:ss")	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")	private Date turnCoinTime;	/**备注**/	@ApiModelProperty(value = "备注")	private String remark;	public String getDirectionFormatter() {	    if(null == directionFormatter || "".equals(directionFormatter)){		    return DIRECTION.getValue(getDirection());		}	    return this.directionFormatter;	}	public void setDirectionFormatter(String directionFormatter) {	    this.directionFormatter=directionFormatter;	}	public String getStateFormatter() {	    if(null == stateFormatter || "".equals(stateFormatter)){		    return STATE.getValue(getState());		}	    return this.stateFormatter;	}	public void setStateFormatter(String stateFormatter) {	    this.stateFormatter=stateFormatter;	}	/**1,平台到母账号:toaccount<br>2,母账号到平台:toplatform**/	public enum DIRECTION {			/**1,平台到母账号:toaccount**/		TOACCOUNT("平台到母账号",1),			/**2,母账号到平台:toplatform**/		TOPLATFORM("母账号到平台",2);			public final int code;		public final String value;		private static Map<Integer, String> map = new HashMap<Integer, String>();			private DIRECTION(String value, int code) {			this.code = code;			this.value = value;		}			public static String getValue(Integer code) {			if (null == code) {				return null;			}			for (DIRECTION direction : DIRECTION.values()) {				if (direction.code == code) { 					return direction.value;				}			}			return null;		}			public static Integer getCode(String value) {			if (null == value  || "".equals(value)) {					return null;			}			for (DIRECTION direction : DIRECTION.values()) {				if (direction.value.equals(value)) { 					return direction.code;				}			}			return null;		}			public static  Map<Integer, String> getEnumMap() {			if(map.size() == 0){				for (DIRECTION direction : DIRECTION.values()) {					map.put(direction.code,direction.value);				}			}			return map;		}	}		/**0,正常:normal<br>1,作废:delete**/	public enum STATE {			/**0,正常:normal**/		NORMAL("正常",0),			/**1,作废:delete**/		DELETE("作废",1);			public final int code;		public final String value;		private static Map<Integer, String> map = new HashMap<Integer, String>();			private STATE(String value, int code) {			this.code = code;			this.value = value;		}			public static String getValue(Integer code) {			if (null == code) {				return null;			}			for (STATE state : STATE.values()) {				if (state.code == code) { 					return state.value;				}			}			return null;		}			public static Integer getCode(String value) {			if (null == value  || "".equals(value)) {					return null;			}			for (STATE state : STATE.values()) {				if (state.value.equals(value)) { 					return state.code;				}			}			return null;		}			public static  Map<Integer, String> getEnumMap() {			if(map.size() == 0){				for (STATE state : STATE.values()) {					map.put(state.code,state.value);				}			}			return map;		}	}	    public PlatAssetLog(){}    public PlatAssetLog(Consumer<PlatAssetLog> consumer){    consumer.accept(this);    }}