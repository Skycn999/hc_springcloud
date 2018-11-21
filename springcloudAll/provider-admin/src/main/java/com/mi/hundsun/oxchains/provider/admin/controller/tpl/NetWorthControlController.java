package com.mi.hundsun.oxchains.provider.admin.controller.tpl;


import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.tpl.NetWorthControl;
import com.mi.hundsun.oxchains.base.core.service.tpl.NetWorthControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("净值风控模版服务")
@Slf4j
@RestController
@RequestMapping("/tpl/netWorth")
public class NetWorthControlController {

    @Autowired
    NetWorthControlService netWorthControlService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return netWorthControlService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<NetWorthControl> select(NetWorthControl netWorthControl) throws Exception {
        return netWorthControlService.select(netWorthControl);
    }

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insertNetWorthControl(@RequestBody NetWorthControl netWorthControl) throws Exception {
        netWorthControlService.insertNetWorthControl(netWorthControl);
    }

//    @ApiOperation(value = "查询信息")
//    @PostMapping(value = "/selectOne")
//    public NetWorthControl selectOne(@RequestBody NetWorthControl netWorthControl) throws BussinessException {
//        return netWorthControlService.selectOne(netWorthControl);
//    }

    @ApiOperation(value = "编辑更新信息")
    @PostMapping(value = "/updateNetWorthControl")
    public void updateNetWorthControl(@RequestBody NetWorthControl netWorthControl) throws Exception {
        netWorthControlService.updateNetWorthControl(netWorthControl);
    }

    @ApiOperation(value = "获取启用中默认的模板，若无默认，则获取最新启用的模板")
    @PostMapping(value = "/findNetWorthControlIsDefault")
    public NetWorthControl findNetWorthControlIsDefault() throws Exception {
       return  netWorthControlService.findNetWorthControlIsDefault();
    }
}
