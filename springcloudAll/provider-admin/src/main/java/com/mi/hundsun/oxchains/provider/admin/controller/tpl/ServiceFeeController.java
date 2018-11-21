package com.mi.hundsun.oxchains.provider.admin.controller.tpl;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.base.core.service.tpl.ServiceFeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("手续费模板服务")
@Slf4j
@RestController
@RequestMapping("/tpl/serviceFee")
public class ServiceFeeController {
    @Autowired
    ServiceFeeService serviceFeeService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return serviceFeeService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<ServiceFee> select(@RequestBody ServiceFee serviceFee) throws Exception {
        return serviceFeeService.select(serviceFee);
    }

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insertFee")
    public void insertFee(@RequestBody ServiceFee serviceFee) throws Exception {
        serviceFeeService.insertFee(serviceFee);
    }

    @ApiOperation(value = "编辑更新信息")
    @PostMapping(value = "/updateServiceFeee")
    public void updateServiceFeee(@RequestBody ServiceFee serviceFee) throws Exception {
        serviceFeeService.updateServiceFeee(serviceFee);
    }

    @ApiOperation(value = "获取启用中默认的手续费模板，若无默认，则获取最新启用的模板")
    @PostMapping(value = "/findServiceFeeIsDefault")
    public ServiceFee findServiceFeeIsDefault(@RequestParam String coinCode) throws Exception {
        return serviceFeeService.findServiceFeeIsDefault(coinCode);
    }

}
