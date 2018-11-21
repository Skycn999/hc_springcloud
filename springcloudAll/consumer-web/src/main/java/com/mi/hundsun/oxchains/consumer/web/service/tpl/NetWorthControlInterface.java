package com.mi.hundsun.oxchains.consumer.web.service.tpl;

import com.mi.hundsun.oxchains.base.core.po.tpl.NetWorthControl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("provider-admin-${feignSuffix}")
public interface NetWorthControlInterface {


    /**
     * 获取启用中默认的模板，若无默认，则获取最新启用的模板
     */
    @PostMapping(value = "/tpl/netWorth/findNetWorthControlIsDefault")
    NetWorthControl findNetWorthControlIsDefault();
}
