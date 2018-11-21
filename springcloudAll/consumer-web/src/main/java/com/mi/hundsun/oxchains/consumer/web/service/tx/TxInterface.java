/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.service.tx;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author 枫亭
 * @date 2018-04-14 18:07.
 */
@FeignClient("provider-trade-${feignSuffix}")
public interface TxInterface {


    @PostMapping(value = "/prod/tx/doTx")
    ResultEntity doTx(@RequestParam("params") String params);

    @PostMapping(value = "/prod/tx/myCurrDelegates")
    ResultEntity myCurrDelegates(@RequestParam("userId") Integer userId, @RequestParam("direction") Integer direction);

    @PostMapping(value = "/prod/tx/myDelegateList")
    ResultEntity myDelegateList(@RequestParam("userId") Integer userId,
                                @RequestParam("direction") Integer direction,
                                @RequestParam("pageNumber") Integer pageNumber,
                                @RequestParam("pageSize") Integer pageSize);

    @PostMapping(value = "/prod/tx/mySubDelegateList")
    ResultEntity mySubDelegateList(@RequestParam("userId") Integer userId,
                                   @RequestParam("direction") Integer direction,
                                   @RequestParam("pageNumber") Integer pageNumber,
                                   @RequestParam("pageSize") Integer pageSize);

    @PostMapping(value = "/prod/tx/myDealList")
    ResultEntity myDealList(@RequestParam("userId") Integer userId,
                            @RequestParam("direction") Integer direction,
                            @RequestParam("pageNumber") Integer pageNumber,
                            @RequestParam("pageSize") Integer pageSize);

    @PostMapping(value = "/prod/tx/mySubDelegateListByMainUuid")
    ResultEntity mySubDelegateListByMainUuid(@RequestParam("uuid") String uuid, @RequestParam("userId") Integer userId);

    @PostMapping(value = "/prod/tx/revoke")
    ResultEntity revoke(@RequestParam("delegateNo") String delegateNo, @RequestParam("symbol") String symbol);

    @PostMapping(value = "/prod/tx/handleMainDelegateOfFailure")
    void handleMainDelegateOfFailure(@RequestBody MainDelegation delegation);

    @PostMapping(value = "/prod/tx/findTradingSubDelegateList")
    ResultEntity findTradingSubDelegateList();

    @PostMapping(value = "/prod/tx/singleQryOrder")
    void singleQryOrder(@RequestParam("map") String map);

    @PostMapping(value = "/prod/tx/findTradingSubDelegateList")
    ResultEntity findTradingMainDelegateList();

    @PostMapping(value = "/prod/tx/syncSubDelegation")
    void syncSubDelegation();

    @PostMapping(value = "/prod/tx/syncMainDelegation")
    void syncMainDelegation();

}
