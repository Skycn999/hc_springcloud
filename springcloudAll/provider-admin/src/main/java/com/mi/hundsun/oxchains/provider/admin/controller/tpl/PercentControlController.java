package com.mi.hundsun.oxchains.provider.admin.controller.tpl;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.tpl.PercentControl;
import com.mi.hundsun.oxchains.base.core.service.tpl.PercentControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("净值风控模板服务")
@Slf4j
@RestController
@RequestMapping("/tpl/percentControl")
public class PercentControlController {
    @Autowired
    PercentControlService percentControlService;

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<PercentControl> select(PercentControl percentControl) throws Exception {
        return percentControlService.select(percentControl);
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return percentControlService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insertPercentControl(@RequestBody PercentControl percentControl) throws Exception {
        percentControlService.insert(percentControl);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody PercentControl percentControl) throws BussinessException {
        percentControlService.updateByPrimaryKeySelective(percentControl);
    }
}
