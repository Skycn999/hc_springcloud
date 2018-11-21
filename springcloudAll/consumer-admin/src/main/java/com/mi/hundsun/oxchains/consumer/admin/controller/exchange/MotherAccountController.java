package com.mi.hundsun.oxchains.consumer.admin.controller.exchange;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.RSAUtils;
import com.mi.hundsun.oxchains.base.core.po.exchange.Exchange;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.ExchangeInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.MotherAccountInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 交易所母账号controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class MotherAccountController extends BaseController {
    @Autowired
    ExchangeInterface exchangeInterface;
    @Autowired
    MotherAccountInterface motherAccountInterface;

    /**
     * 交易所母账号列表
     * @param modelMap
     * @return
     */
    @RequestMapping("motherAccount/list")
    public String list(ModelMap modelMap) throws Exception {
        Exchange exchange = new Exchange();
        exchange.setState(Exchange.STATE.ENABLE.code);
        exchange.setDelFlag(GenericPo.DELFLAG.NO.code);
        List<Exchange> exchanges = exchangeInterface.select(exchange);
        ResultEntity resultEntity =  motherAccountInterface.getRsaPublicKey();
        if(resultEntity.getCode()== ResultEntity.SUCCESS){
            modelMap.put("key",resultEntity.getData().toString());
        }
        modelMap.put("exchanges",exchanges);
        return ok("exchange/motherAccount/list");
    }

}
