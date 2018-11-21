/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.controller;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.service.tx.DealOrderService;
import com.mi.hundsun.oxchains.base.core.tx.model.CountDealOrderModel;
import com.mi.hundsun.oxchains.base.core.tx.model.DealOrderModel;
import com.mi.hundsun.oxchains.base.core.tx.po.DealOrder;
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
 *
 * @author 枫亭
 * @description
 * @date 2018-04-08 20:49.
 */
@Slf4j
@RestController
@RequestMapping("/tx/dealOrder")
public class DealOrderController extends GenericController {

    @Autowired
    DealOrderService dealOrderService;

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insert(@RequestBody DealOrder dealOrder) throws BussinessException {
        dealOrderService.insert(dealOrder);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody DealOrder dealOrder) throws BussinessException {
        dealOrderService.updateByPrimaryKeySelective(dealOrder);
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        dealOrderService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/removeById")
    public void removeById(@RequestBody DealOrder dealOrder) throws BussinessException {
        dealOrderService.removeById(dealOrder);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/getNormalModelById")
    public DealOrder getNormalModelById(@RequestBody DealOrder dealOrder) throws BussinessException {
        return dealOrderService.getNormalModelById(dealOrder);
    }

    @ApiOperation(value = "uuid查询")
    @PostMapping(value = "/getNormalModelByUuid")
    public DealOrder getNormalModelByUuid(@RequestBody String uuid) throws BussinessException {
        DealOrder dealOrder = new DealOrder();
        dealOrder.setUuid(uuid);
        return dealOrderService.selectOne(dealOrder);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/selectOne")
    public DealOrder selectOne(@RequestBody DealOrder dealOrder) throws BussinessException {
        return dealOrderService.selectOne(dealOrder);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<DealOrder> select(@RequestBody DealOrder dealOrder) throws BussinessException {
        return dealOrderService.select(dealOrder);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/selectAll")
    public List<DealOrder> selectAll() throws BussinessException {
        return dealOrderService.selectAll();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return dealOrderService.getDtGridList(dtGridPager, DealOrderModel.class);
    }

    @ApiOperation(value = "统计买入卖出笔数")
    @PostMapping(value = "/countDirection")
    public List<CountDealOrderModel> countDirection() {
        return dealOrderService.countDirection();
    }

    @ApiOperation(value = "")
    @PostMapping(value = "/countTodayTxVolume")
    public List<CountDealOrderModel> countTodayTxVolume() {
        return dealOrderService.countTodayTxVolume();
    }

}
