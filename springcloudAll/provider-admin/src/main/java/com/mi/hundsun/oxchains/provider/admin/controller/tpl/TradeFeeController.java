package com.mi.hundsun.oxchains.provider.admin.controller.tpl;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.tpl.TradeFee;
import com.mi.hundsun.oxchains.base.core.service.tpl.TradeFeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("交易手续费率模版")
@Slf4j
@RestController
@RequestMapping("/tpl/tradeFee")
public class TradeFeeController {

    @Autowired
    TradeFeeService tradeFeeService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return tradeFeeService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insertTradeFee(@RequestBody TradeFee tradeFee) throws Exception {
        tradeFeeService.insert(tradeFee);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody TradeFee tradeFee) throws BussinessException {
        tradeFeeService.updateByPrimaryKeySelective(tradeFee);
    }

    @ApiOperation(value = "查询单个信息")
    @PostMapping(value = "/selectOne")
    public TradeFee selectOne(@RequestBody TradeFee tradeFee) throws BussinessException {
        return tradeFeeService.selectOne(tradeFee);
    }

    @ApiOperation(value = "查询信息")
    @PostMapping(value = "/select")
    public List<TradeFee> select(@RequestBody TradeFee tradeFee) throws BussinessException {
        return tradeFeeService.select(tradeFee);
    }
}
