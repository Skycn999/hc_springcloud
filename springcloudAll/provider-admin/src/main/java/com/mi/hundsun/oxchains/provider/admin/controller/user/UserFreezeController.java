package com.mi.hundsun.oxchains.provider.admin.controller.user;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.user.UserFreezeModel;
import com.mi.hundsun.oxchains.base.core.po.user.UserFreeze;
import com.mi.hundsun.oxchains.base.core.service.user.UserFreezeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api("用户冻结")
@Slf4j
@RestController
@RequestMapping("/user/userFreeze")
public class UserFreezeController {

    @Autowired
    UserFreezeService userFreezeService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return userFreezeService.getDtGridList(dtGridPager,UserFreezeModel.class);
    }

    @ApiOperation(value = "查询用户冻结信息")
    @PostMapping(value = "/selectOne")
    public UserFreeze selectOne(@RequestBody UserFreeze userFreeze) throws BussinessException {
        return userFreezeService.selectOne(userFreeze);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody UserFreeze userFreeze) throws BussinessException {
        userFreezeService.updateByPrimaryKeySelective(userFreeze);
    }


      @ApiOperation(value = "导出excel")
      @PostMapping(value = "/getDtGridListExport")
      public DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws Exception {
          return userFreezeService.getDtGridListExport(dtGridPager,UserFreezeModel.class);
         }
}
