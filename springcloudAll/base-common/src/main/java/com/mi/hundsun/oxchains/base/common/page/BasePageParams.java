package com.mi.hundsun.oxchains.base.common.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页请求参数
 */
@Data
@ApiModel(description = "分页请求参数")
public class BasePageParams {
    /**
     * 每个列表记录数量
     */
    @ApiModelProperty(value = "每个列表记录数量(默认20)")
    private Integer rows;
    /**
     * 分类
     */
    @ApiModelProperty(value = "分类(可空)")
    private String sort;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序(可空) asc,desc")
    private String order;
    /**
     * 开始页码
     */
    @ApiModelProperty(value = "开始页码(默认1)")
    private Integer star = 1;

    public static final int DEFAULT_ROWS = 20;

    /**
     * 每个列表记录数量
     */
    public Integer getRows() {
        return (null == rows || rows < 1) ? 20 : rows;
    }

    /**
     * 开始记录数
     */
    public int getNum() {
        return 0;
    }
}
