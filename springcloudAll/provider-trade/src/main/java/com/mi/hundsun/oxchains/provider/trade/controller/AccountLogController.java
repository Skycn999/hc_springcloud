/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.controller;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.service.tx.AccountService;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import io.swagger.annotations.Api;
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
@Api("资产变更记录管理服务")
@Slf4j
@RestController
@RequestMapping("/tx/accountLog")
public class AccountLogController extends GenericController{

    @Autowired
    AccountService accountService;

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insert(@RequestBody Account account) throws BussinessException {
        accountService.insert(account);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Account account) throws BussinessException {
        accountService.updateByPrimaryKeySelective(account);
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        accountService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/removeById")
    public void removeById(@RequestBody Account account) throws BussinessException {
        accountService.removeById(account);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/getNormalModelById")
    public Account getNormalModelById(@RequestBody Account account) throws BussinessException {
        return accountService.getNormalModelById(account);
    }

    @ApiOperation(value = "uuid查询")
    @PostMapping(value = "/getNormalModelByUuid")
    public Account getNormalModelByUuid(@RequestBody String uuid) throws BussinessException {
        Account account = new Account();
        account.setUuid(uuid);
        return accountService.selectOne(account);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/selectOne")
    public Account selectOne(@RequestBody Account account) throws BussinessException {
        return accountService.selectOne(account);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<Account> select(@RequestBody Account account) throws BussinessException {
        return accountService.select(account);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/selectAll")
    public List<Account> selectAll() throws BussinessException {
        return accountService.selectAll();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return accountService.getDtGridList(dtGridPager);
    }



    

}
