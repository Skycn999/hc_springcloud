package com.mi.hundsun.oxchains.provider.admin.controller.user;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.user.UserIdentifyModel;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import com.mi.hundsun.oxchains.base.core.service.user.UserIdentifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("用户认证")
@Slf4j
@RestController
@RequestMapping("/user/userIdentify")
public class UserIdentifyController {

    @Autowired
    UserIdentifyService userIdentifyService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return userIdentifyService.getDtGridList(dtGridPager,UserIdentifyModel.class);
    }

    @ApiOperation(value = "查询用户认证信息")
    @PostMapping(value = "/selectOne")
    public UserIdentify selectOne(@RequestBody UserIdentify userIdentify) throws BussinessException {
        return userIdentifyService.selectOne(userIdentify);
    }

    @ApiOperation(value = "审核信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody UserIdentify userIdentify) throws BussinessException {
        userIdentifyService.updateByPrimaryKeySelective(userIdentify);
    }

    @ApiOperation(value = "导出excel")
    @PostMapping(value = "/getDtGridListExport")
    public DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws Exception {
        return userIdentifyService.getDtGridListExport(dtGridPager,UserIdentify.class);
    }
}
