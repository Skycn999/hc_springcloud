package com.mi.hundsun.oxchains.base.core.po.system;import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;import lombok.Data;import lombok.EqualsAndHashCode;import io.swagger.annotations.ApiModel;import io.swagger.annotations.ApiModelProperty;import java.util.function.Consumer;import javax.persistence.Table;import javax.persistence.Transient;import  java.util.Map;import  java.util.Date;import  java.util.HashMap;import  com.alibaba.fastjson.annotation.JSONField;/** * 数据字典实体信息<br> * * @author bin * @date   2018-04-23 11:11:35 */@Data@EqualsAndHashCode(callSuper = true)@ApiModel(description = "数据字典")@Table(name = "s_dict")public class Dict extends GenericPo<Integer> {	public static final String TABLE_NAME = "s_dict";	/**标识**/	@ApiModelProperty(value = "标识")	private String nid;	/**名称**/	@ApiModelProperty(value = "名称")	private String name;	/**值**/	@ApiModelProperty(value = "值")	private String val;	/**排序**/	@ApiModelProperty(value = "排序")	private Integer sort;	/**状态 1,启用:enable;0,停用:disable**/	@ApiModelProperty(value = "状态 1,启用:enable;0,停用:disable")	private Integer state;	@Transient	private String stateFormatter ;	/**描述**/	@ApiModelProperty(value = "描述")	private String desc;	public String getStateFormatter() {		if(null == stateFormatter || "".equals(stateFormatter)){			return STATE.getValue(getState());		}		return this.stateFormatter;	}	public void setStateFormatter(String stateFormatter) {		this.stateFormatter=stateFormatter;	}	/**1,启用:enable<br>0,停用:disable**/	public enum STATE {		/**1,启用:enable**/		ENABLE("启用",1),		/**0,停用:disable**/		DISABLE("停用",0);		public final int code;		public final String value;		private static Map<Integer, String> map = new HashMap<Integer, String>();		private STATE(String value, int code) {			this.code = code;			this.value = value;		}		public static String getValue(Integer code) {			if (null == code) {				return null;			}			for (STATE state : STATE.values()) {				if (state.code == code) {					return state.value;				}			}			return null;		}		public static Integer getCode(String value) {			if (null == value  || "".equals(value)) {				return null;			}			for (STATE state : STATE.values()) {				if (state.value.equals(value)) {					return state.code;				}			}			return null;		}		public static  Map<Integer, String> getEnumMap() {			if(map.size() == 0){				for (STATE state : STATE.values()) {					map.put(state.code,state.value);				}			}			return map;		}	}	public Dict(){}	public Dict(Consumer<Dict> consumer){		consumer.accept(this);	}}