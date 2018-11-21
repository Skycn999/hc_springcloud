package com.mi.hundsun.oxchains.provider.admin.controller.count;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.model.count.CountEarningsModel;
import com.mi.hundsun.oxchains.base.core.service.count.CountEarningsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("收益汇总")
@Slf4j
@RestController
@RequestMapping("/count/earnings")
public class EarningsController {

    @Autowired
    CountEarningsService countEarningsService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return countEarningsService.getDtGridList(dtGridPager,CountEarningsModel.class);
    }
}
