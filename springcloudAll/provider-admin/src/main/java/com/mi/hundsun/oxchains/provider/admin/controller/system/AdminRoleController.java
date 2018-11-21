package com.mi.hundsun.oxchains.provider.admin.controller.system;

import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.AdminRole;
import com.mi.hundsun.oxchains.base.core.service.system.AdminRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("管理员与角色关联相关服务")
@Slf4j
@RestController
@RequestMapping("/sys/adminRole")
public class AdminRoleController {
    @Autowired
    AdminRoleService adminRoleService;

    @ApiOperation(value = "查询管理员与角色关联信息")
    @PostMapping(value = "/select")
    public List<AdminRole> select(@RequestBody AdminRole adminRole) throws BussinessException {
        return adminRoleService.select(adminRole);
    }
}
