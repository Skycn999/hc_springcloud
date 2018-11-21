package com.mi.hundsun.oxchains.provider.user.controller;

import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.service.user.UserRiskControlService;
import com.mi.hundsun.oxchains.base.core.service.user.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户风控相关服务
 */
@Api(value = "用户风控相关服务", description = "UserRiskControlController Created By 枫亭 at 2018-04-08 20:58")
@RestController
@RequestMapping("/prod/user/riskControl")
public class UserRiskControlController extends GenericController {
    @Autowired
    UsersService usersService;
    @Autowired
    UserRiskControlService userRiskControlService;

    @ApiOperation(value = "校验手续费是否小于给用户设置提现手续费金额(防篡改)")
    @PostMapping("/checkUserServiceFeeTpl")
    public boolean checkUserServiceFeeTpl(@RequestParam String code, @RequestParam String serviceFee) throws Exception {
        return userRiskControlService.checkUserServiceFeeTpl(code, serviceFee);
    }
}
