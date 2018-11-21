package com.mi.hundsun.oxchains.consumer.admin.service.exchange;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.exchange.Exchange;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface ExchangeInterface {


    /**
     * 交易所分页查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/exchange/exchange/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws Exception;

    /**
     * 交易所列表查询
     *
     * @param exchange
     * @return
     */
    @PostMapping(value = "/exchange/exchange/select")
    List<Exchange> select(@RequestBody Exchange exchange) throws Exception;

    /**
     * 添加交易所
     *
     * @param exchange
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/exchange/exchange/insert")
    ResultEntity insert(@RequestBody Exchange exchange) throws Exception;

    /**
     * 编辑交易所
     *
     * @param exchange
     */
    @PostMapping(value = "/exchange/exchange/updateExchange")
    ResultEntity updateExchange(@RequestBody Exchange exchange)throws Exception;
}
