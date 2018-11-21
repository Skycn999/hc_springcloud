package com.mi.hundsun.oxchains.provider.admin.controller.count;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.model.count.CountUserAccountModel;
import com.mi.hundsun.oxchains.base.core.service.count.CountUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("用户持仓汇总")
@Slf4j
@RestController
@RequestMapping("/count/userAccount")
public class UserAccountController {

    @Autowired
    CountUserAccountService countUserAccountService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return countUserAccountService.getDtGridList(dtGridPager,CountUserAccountModel.class);
    }
}
