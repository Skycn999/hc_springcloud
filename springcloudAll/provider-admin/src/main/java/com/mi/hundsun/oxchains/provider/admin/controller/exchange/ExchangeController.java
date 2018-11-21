package com.mi.hundsun.oxchains.provider.admin.controller.exchange;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.exchange.Exchange;
import com.mi.hundsun.oxchains.base.core.service.exchange.ExchangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("交易所管理服务")
@Slf4j
@RestController
public class ExchangeController {
    @Autowired
    ExchangeService exchangeService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/exchange/exchange/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return exchangeService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "查询列表")
    @PostMapping(value = "/exchange/exchange/select")
    public List<Exchange> select(@RequestBody Exchange exchange) throws Exception {
        return exchangeService.select(exchange);
    }

    @ApiOperation(value = "新增交易所")
    @PostMapping(value = "/exchange/exchange/insert")
    public ResultEntity insert(@RequestBody Exchange exchange) throws Exception {
        try {
            exchangeService.insert(exchange);
            return  ResultEntity.success();
        }catch (Exception e){
            e.printStackTrace();
            return  ResultEntity.fail();
        }
    }

    @ApiOperation(value = "编辑交易所")
    @PostMapping(value = "/exchange/exchange/updateExchange")
    public ResultEntity updateExchange(@RequestBody Exchange exchange) throws Exception {
        try {
            exchangeService.updateByPrimaryKeySelective(exchange);
            return  ResultEntity.success();
        }catch (Exception e){
            e.printStackTrace();
            return  ResultEntity.fail();
        }
    }

}
