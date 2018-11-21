package com.mi.hundsun.oxchains.consumer.admin.controller.fn;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.core.po.exchange.Exchange;
import com.mi.hundsun.oxchains.base.core.po.fn.PlatUserAddress;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.ExchangeInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.fn.PlatUserAddressInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 资产划拨记录controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class PlatAssetLogController extends BaseController {
    @Autowired
    ExchangeInterface exchangeInterface;

    @Autowired
    PlatUserAddressInterface platUserAddressInterface;

    @RequestMapping("platAssetLog/list")
    public String list(ModelMap modelMap) throws Exception {
        Exchange exchange = new Exchange();
        exchange.setState(Exchange.STATE.ENABLE.code);
        exchange.setDelFlag(GenericPo.DELFLAG.NO.code);
        List<Exchange> exchanges = exchangeInterface.select(exchange);
        modelMap.put("exchanges",exchanges);
        PlatUserAddress platUserAddress = new PlatUserAddress();
        List<PlatUserAddress> platUserAddresses = platUserAddressInterface.select(platUserAddress);
        platUserAddress.setDelFlag(GenericPo.DELFLAG.NO.code);
        modelMap.put("platUserAddress",platUserAddresses);
        return ok("fn/platAssetLog/list");
    }
}
