/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.controller;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.service.tx.SubDelegationService;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 交易服务控制器
 * @author 枫亭
 * @description
 * @date 2018-04-08 20:49.
 */
@Slf4j
@RestController
@RequestMapping("/tx/subDelegation")
public class SubDelegationController extends GenericController{

    @Autowired
    SubDelegationService subDelegationService;

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insert(@RequestBody SubDelegation subDelegation) throws BussinessException {
        subDelegationService.insert(subDelegation);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody SubDelegation subDelegation) throws BussinessException {
        subDelegationService.updateByPrimaryKeySelective(subDelegation);
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        subDelegationService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/removeById")
    public void removeById(@RequestBody SubDelegation subDelegation) throws BussinessException {
        subDelegationService.removeById(subDelegation);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/getNormalModelById")
    public SubDelegation getNormalModelById(@RequestBody SubDelegation subDelegation) throws BussinessException {
        return subDelegationService.getNormalModelById(subDelegation);
    }

    @ApiOperation(value = "uuid查询")
    @PostMapping(value = "/getNormalModelByUuid")
    public SubDelegation getNormalModelByUuid(@RequestBody String uuid) throws BussinessException {
        SubDelegation subDelegation = new SubDelegation();
        subDelegation.setUuid(uuid);
        return subDelegationService.selectOne(subDelegation);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/selectOne")
    public SubDelegation selectOne(@RequestBody SubDelegation subDelegation) throws BussinessException {
        return subDelegationService.selectOne(subDelegation);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<SubDelegation> select(@RequestBody SubDelegation subDelegation) throws BussinessException {
        return subDelegationService.select(subDelegation);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/selectAll")
    public List<SubDelegation> selectAll() throws BussinessException {
        return subDelegationService.selectAll();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return subDelegationService.getDtGridList(dtGridPager);
    }



}
