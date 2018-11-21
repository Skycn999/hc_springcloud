package com.mi.hundsun.oxchains.consumer.admin.service.count;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-admin-${feignSuffix}")
public interface UserAccountInterface {

    /**
     * 用户持仓汇总查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/count/userAccount/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;
}
