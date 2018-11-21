package com.mi.hundsun.oxchains.consumer.web.service.tpl;

import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-admin-${feignSuffix}")
public interface ServiceFeeInterface {

    /**
     * 获取启用中默认的手续费模板，若无默认，则获取最新启用的模板
     * @return
     */
    @PostMapping(value = "/tpl/serviceFee/findServiceFeeIsDefault")
    ServiceFee findServiceFeeIsDefault(@RequestParam("coinCode") String coinCode);
}
