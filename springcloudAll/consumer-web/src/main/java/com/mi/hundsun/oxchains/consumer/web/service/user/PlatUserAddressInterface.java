/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.service.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 枫亭
 * @description TODO
 * @date 2018-04-19 17:41.
 */
@FeignClient("provider-user-${feignSuffix}")
public interface PlatUserAddressInterface {

    @PostMapping(value = "/prod/user/platUserAddress/getByUserIdAndCode")
    ResultEntity getByUserIdAndCode(@RequestParam("userId") Integer userId, @RequestParam("code") String code);

}
