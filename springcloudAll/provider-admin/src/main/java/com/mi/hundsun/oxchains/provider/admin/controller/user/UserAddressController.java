package com.mi.hundsun.oxchains.provider.admin.controller.user;


import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.user.UserAddressModel;
import com.mi.hundsun.oxchains.base.core.po.user.UserAddress;
import com.mi.hundsun.oxchains.base.core.service.user.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("用户地址")
@Slf4j
@RestController
@RequestMapping("user/userAddress")
public class UserAddressController {

    @Autowired
    UserAddressService userAddressService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return userAddressService.getDtGridList(dtGridPager,UserAddressModel.class);
    }

    @ApiOperation(value = "查询单个用户地址信息")
    @PostMapping(value = "/selectOne")
    public UserAddress selectOne(@RequestBody UserAddress userAddress) throws BussinessException {
        return userAddressService.selectOne(userAddress);
    }

//    @ApiOperation(value = "查询单个用户信息")
//    @PostMapping(value = "/selectByPrimaryKey")
//    public UserAddress selectByPrimaryKey(@RequestBody Integer pk) throws BussinessException {
//        return userAddressService.selectByPrimaryKey(pk);
//    }

    @ApiOperation(value = "导出excel")
    @PostMapping(value = "/getDtGridListExport")
    public DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws Exception {
        return userAddressService.getDtGridListExport(dtGridPager,UserAddressModel.class);
    }
}
