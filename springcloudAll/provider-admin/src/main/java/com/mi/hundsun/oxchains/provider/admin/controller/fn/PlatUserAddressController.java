package com.mi.hundsun.oxchains.provider.admin.controller.fn;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.po.fn.PlatUserAddress;
import com.mi.hundsun.oxchains.base.core.service.fn.PlatUserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api("平台地址管理服务")
@Slf4j
@RestController
@RequestMapping("/fn/platUserAddress")
public class PlatUserAddressController extends GenericController {
    @Autowired
    PlatUserAddressService platUserAddressService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return platUserAddressService.getDtGridList(dtGridPager);
    }


    @ApiOperation(value = "查询列表")
    @PostMapping(value = "/select")
    public List<PlatUserAddress> select(@RequestBody PlatUserAddress platUserAddress) throws Exception {
        return platUserAddressService.select(platUserAddress);
    }

    @ApiOperation(value = "新增平台地址")
    @PostMapping(value = "/insert")
    public ResultEntity insert(@RequestBody PlatUserAddress platUserAddress) throws Exception {
        try {
            platUserAddressService.insert(platUserAddress);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    @ApiOperation(value = "查询统计")
    @PostMapping(value = "/selectCount")
    public ResultEntity selectCount(@RequestBody PlatUserAddress platUserAddress) throws Exception {
        try {
            int c = platUserAddressService.selectCount(platUserAddress);
            return ok(c);
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    @ApiOperation(value = "获取平台地址信息")
    @PostMapping(value = "/selectOne")
    public PlatUserAddress selectOne(@RequestBody PlatUserAddress platUserAddress) throws Exception {
        PlatUserAddress userAddress = platUserAddressService.selectOne(platUserAddress);
        return userAddress;
}

}
