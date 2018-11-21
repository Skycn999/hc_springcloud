package com.mi.hundsun.oxchains.provider.admin.controller.fn;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.PlatAssetLog;
import com.mi.hundsun.oxchains.base.core.service.fn.PlatAssetLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("资产划拨记录服务")
@Slf4j
@RestController
public class PlatAssetLogController {
    @Autowired
    PlatAssetLogService platAssetLogService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/fn/platAssetLog/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return platAssetLogService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "新增记录")
    @PostMapping(value = "/fn/platAssetLog/insert")
    public void insert(@RequestBody PlatAssetLog platAssetLog) throws BussinessException {
        platAssetLogService.insert(platAssetLog);
    }


    @ApiOperation(value = "作废记录")
    @PostMapping(value = "/fn/platAssetLog/removeById")
    public void removeById(@RequestBody PlatAssetLog platAssetLog) throws BussinessException {
        platAssetLogService.updateByPrimaryKeySelective(platAssetLog);
    }

    @ApiOperation(value = "导出excel")
    @PostMapping(value = "/fn/platAssetLog/getDtGridListExport")
    public DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws Exception {
        return platAssetLogService.getDtGridListExport(dtGridPager);
    }
}
