package com.mi.hundsun.oxchains.consumer.admin.service.tx;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-trade-${feignSuffix}")
public interface SubDelegationInterface {

    @PostMapping(value = "/tx/subDelegation/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager)throws Exception;
}
