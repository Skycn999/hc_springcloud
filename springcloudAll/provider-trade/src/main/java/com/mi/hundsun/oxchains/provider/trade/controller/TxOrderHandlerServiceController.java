/*
 * Copyright (c) 2015-2018, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.controller;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.model.quote.model.OrderQryRes;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import com.mi.hundsun.oxchains.provider.trade.service.TxOrderHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author 枫亭
 * @description TODO
 * @date 2018-06-14 17:41.
 */
@Slf4j
@RestController
@RequestMapping("/prod/tx/handle")
public class TxOrderHandlerServiceController {
    @Autowired
    private TxOrderHandlerService txOrderHandlerService;

    @PostMapping(value = "/handleSucceedOrderOfMarketSellOut")
    public void handleSucceedOrderOfMarketSellOut(SubDelegation sub, OrderQryRes result, String entrustNo) {

    }

    
    public void handleSucceedOrderOfMarketBuyIn(SubDelegation sub, OrderQryRes result, String entrustNo) {

    }

    
    public void handleSucceedOrderOfLimitSellOut(SubDelegation sub, OrderQryRes result, String entrustNo) {

    }

    public void handleSucceedOrderOfLimitBuyIn(SubDelegation sub, OrderQryRes result, String entrustNo) {

    }

    public void handlerFailedOrderOfLimitedBuyIn(SubDelegation sub, String msg) {

    }

    public void handlerFailedOrderOfLimitedSellOut(SubDelegation sub, String msg) {

    }

    public void handlerFailedOrderOfMarketBuyIn(SubDelegation sub, String msg) {

    }

    public void handlerFailedOrderOfMarketSellOut(SubDelegation sub, String msg) {

    }

    public void handleMainDelegateOfFailureByLimitedBuyIn(MainDelegation delegation) {

    }

    public void handleMainDelegateOfFailureByLimitedSellout(MainDelegation delegation) {

    }

    public void handleMainDelegateOfFailureByMarketBuyIn(MainDelegation delegation) {

    }

    public void handleMainDelegateOfFailureByMarketSellout(MainDelegation delegation) {

    }

    public void handlerRevokeOrderOfLimitedBuyIn(SubDelegation sub, String msg) {

    }

    public void handlerRevokeOrderOfLimitedSellOut(SubDelegation sub, String msg) {

    }

    public void handlerRevokeOrderOfMarketBuyIn(SubDelegation sub, String msg) {

    }

    public void handlerRevokeOrderOfMarketSellOut(SubDelegation sub, String msg) {

    }
}
