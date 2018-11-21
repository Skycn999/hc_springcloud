package com.mi.hundsun.oxchains.consumer.admin.service.tx;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.tx.model.CountDealOrderModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-trade-${feignSuffix}")
public interface DealOrderInterface {

    /**
     * 用户资产持仓分页查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/tx/dealOrder/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws Exception;

    @PostMapping(value = "/tx/dealOrder/countDirection")
    List<CountDealOrderModel> countDirection();
}
