/*
 * Copyright (c) 2015-2018, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.quartz.service;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author 枫亭
 * @date 2018-06-14 14:21.
 */
@FeignClient("${}")
public interface CountTransactionInterface {
}
