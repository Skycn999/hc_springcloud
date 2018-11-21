package com.mi.hundsun.oxchains.provider.admin.controller.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.base.core.service.system.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("系统用户相关服务")
@Slf4j
@RestController
@RequestMapping("/sys/admin")
public class AdminController {

    @Autowired
    AdminService adminService;
//
//    @ApiOperation(value = "新增管理员")
//    @PostMapping(value = "/saveAdmin")
//    public Admin saveAdmin(@RequestBody Admin admin) throws BussinessException {
//        return adminService.saveAdmin(admin);
//    }
//
//    @ApiOperation(value = "修改管理员")
//    @PostMapping(value = "/updateAdmin")
//    public void updateAdmin(@RequestBody Admin admin) throws BussinessException {
//        adminService.updateAdmin(admin);
//    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return adminService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "更新管理员信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Admin admin) throws BussinessException {
        adminService.updateByPrimaryKeySelective(admin);
    }

    @ApiOperation(value = "物理删除管理员信息")
    @PostMapping(value = "/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        adminService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "查询管理员信息")
    @PostMapping(value = "/selectOne")
    public Admin selectOne(@RequestBody Admin admin) throws BussinessException {
        return adminService.selectOne(admin);
    }
}
