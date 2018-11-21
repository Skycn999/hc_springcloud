package com.mi.hundsun.oxchains.provider.admin.controller.exchange;


import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.exchange.CommodityModel;
import com.mi.hundsun.oxchains.base.core.po.quote.Commodity;
import com.mi.hundsun.oxchains.base.core.service.quote.CommodityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("代码资料管理服务")
@Slf4j
@RestController
public class CommodityController  extends GenericController {

    @Autowired
    CommodityService commodityService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/exchange/commodity/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return commodityService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "查询用户资产信息列表")
    @PostMapping(value = "/exchange/commodity/select")
    public List<Commodity> select(@RequestBody Commodity commodity) throws BussinessException {
        return commodityService.select(commodity);
    }

    @ApiOperation(value = "查询单个代码资料信息")
    @PostMapping(value = "/exchange/commodity/selectOne")
    public Commodity selectOne(@RequestBody Commodity commodity) throws BussinessException {
        return commodityService.selectOne(commodity);
    }

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/exchange/commodity/insert")
    public void insertFee(@RequestBody Commodity commodity) throws Exception {
        commodityService.insert(commodity);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/exchange/commodity/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Commodity commodity) throws BussinessException {
        commodityService.updateByPrimaryKeySelective(commodity);
    }


    @ApiOperation(value = "校验code")
    @PostMapping(value = "/exchange/commodity/checkCode")
    public boolean checkCode(String code) throws BussinessException {
        Commodity commodity =  commodityService.selectOne(new Commodity(c -> {
            c.setDelFlag(GenericPo.DELFLAG.NO.code);
            c.setCode(code);
        }));
        if(null==commodity){
            return false;
        }
        return true;
    }
}
