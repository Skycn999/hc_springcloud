package com.mi.hundsun.oxchains.consumer.web.service.tpl;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.tpl.Protocol;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("provider-user-${feignSuffix}")
public interface ProtocolInterface {

    /**
     * 查询协议
     *
     * @return
     */
    @PostMapping(value = "/tpl/protocol/selectOne")
    ResultEntity selectOne(Protocol protocol) throws BussinessException;


}
