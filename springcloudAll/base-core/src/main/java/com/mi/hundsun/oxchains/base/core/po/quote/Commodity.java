/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.base.core.po.quote;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author 枫亭
 * @description TODO
 * @date 2018-04-12 17:34.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "保存代码的资料信息")
@Table(name = "ex_commodity")
public class Commodity extends GenericPo<Integer> {

    public static final String TABLE_NAME = "ex_commodity";

    /**
     * 代码
     **/
    @ApiModelProperty(value = "代码")
    private String code;
    /**
     * 英文描述介绍
     **/
    @ApiModelProperty(value = "英文描述介绍")
    private String enDesc;
    /**
     * 中文描述介绍
     **/
    @ApiModelProperty(value = "中文描述介绍")
    private String cnDesc;

    public Commodity(){}

    public Commodity(Consumer<Commodity> consumer) {
        consumer.accept(this);
    }

}