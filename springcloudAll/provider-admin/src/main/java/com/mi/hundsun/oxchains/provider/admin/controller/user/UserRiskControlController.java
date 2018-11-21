package com.mi.hundsun.oxchains.provider.admin.controller.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.user.UserRiskControlModel;
import com.mi.hundsun.oxchains.base.core.po.user.UserRiskControl;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.user.UserRiskControlService;
import com.mi.hundsun.oxchains.base.core.service.user.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Api("用户风控设置服务")
@Slf4j
@RestController
@RequestMapping("/user/riskControl")
public class UserRiskControlController extends GenericController {

    @Autowired
    UserRiskControlService userRiskControlService;
    @Autowired
    UsersService usersService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return userRiskControlService.getDtGridList(dtGridPager,UserRiskControlModel.class);
    }

    @ApiOperation(value = "新增风控")
    @PostMapping(value = "/insert")
    public ResultEntity insert(@RequestBody UserRiskControl userRiskControl) throws Exception {
        try {
            userRiskControlService.insertRiskControl(userRiskControl);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }


    @ApiOperation(value = "设置模板")
    @PostMapping(value = "/updateSettingTpl")
    public ResultEntity updateSettingTpl(@RequestBody UserRiskControl userRiskControl) throws BussinessException{
        try {
            userRiskControlService.updateSettingTpl(userRiskControl);
            return ok();
        }catch (BussinessException e){
            e.printStackTrace();
           return fail(e.getMessage());
        }catch (Exception e){
           return fail();
        }
    }

}
