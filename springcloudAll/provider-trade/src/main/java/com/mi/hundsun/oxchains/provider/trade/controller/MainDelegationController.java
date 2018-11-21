/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.controller;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.service.tx.MainDelegationService;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class MainDelegationController extends GenericController{

    @Autowired
    MainDelegationService mainDelegationService;

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/tx/mainDelegation/insert")
    public void insert(@RequestBody MainDelegation mainDelegation) throws BussinessException {
        mainDelegationService.insert(mainDelegation);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/tx/mainDelegation/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody MainDelegation mainDelegation) throws BussinessException {
        mainDelegationService.updateByPrimaryKeySelective(mainDelegation);
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/tx/mainDelegation/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        mainDelegationService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/tx/mainDelegation/removeById")
    public void removeById(@RequestBody MainDelegation mainDelegation) throws BussinessException {
        mainDelegationService.removeById(mainDelegation);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/tx/mainDelegation/getNormalModelById")
    public MainDelegation getNormalModelById(@RequestBody MainDelegation mainDelegation) throws BussinessException {
        return mainDelegationService.getNormalModelById(mainDelegation);
    }

    @ApiOperation(value = "uuid查询")
    @PostMapping(value = "/tx/mainDelegation/getNormalModelByUuid")
    public MainDelegation getNormalModelByUuid(@RequestBody String uuid) throws BussinessException {
        MainDelegation mainDelegation = new MainDelegation();
        mainDelegation.setUuid(uuid);
        return mainDelegationService.selectOne(mainDelegation);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/tx/mainDelegation/selectOne")
    public MainDelegation selectOne(@RequestBody MainDelegation mainDelegation) throws BussinessException {
        return mainDelegationService.selectOne(mainDelegation);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/tx/mainDelegation/select")
    public List<MainDelegation> select(@RequestBody MainDelegation mainDelegation) throws BussinessException {
        return mainDelegationService.select(mainDelegation);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/tx/mainDelegation/selectAll")
    public List<MainDelegation> selectAll() throws BussinessException {
        return mainDelegationService.selectAll();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/tx/mainDelegation/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return mainDelegationService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "连表快速查询")
    @PostMapping(value = "/tx/mainDelegation/getDtGridLists")
    public DtGrid getDtGridLists(@RequestBody DtGrid dtGrid) throws Exception {
        return mainDelegationService.getDtGridList(dtGrid);
    }

    @ApiOperation(value = "导出excel")
    @PostMapping(value = "/tx/mainDelegation/getDtGridListExport")
    public DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws Exception {
        return mainDelegationService.getDtGridListExport(dtGridPager);
    }
}
