package com.mi.hundsun.oxchains.provider.admin.controller.tpl;


import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.tpl.Protocol;
import com.mi.hundsun.oxchains.base.core.service.tpl.ProtocolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("协议")
@Slf4j
@RestController
@RequestMapping("/tpl/protocol")
public class ProtocolController extends GenericController {

    @Autowired
    ProtocolService protocolService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return protocolService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<Protocol> select(Protocol protocol) throws Exception {
        return protocolService.select(protocol);
    }

    @ApiOperation(value = "查询信息")
    @PostMapping(value = "/selectOne")
    public ResultEntity selectOne(@RequestBody Protocol protocol) throws BussinessException {
        try {
            Protocol protocol1 = protocolService.selectOne(protocol);
            return ok(protocol1);
        }catch (Exception e){
            e.printStackTrace();
            return fail();
        }
    }

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insertProtocol(@RequestBody Protocol protocol) throws Exception {
        protocolService.insert(protocol);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Protocol protocol) throws BussinessException {
        protocolService.updateByPrimaryKeySelective(protocol);
    }
}
