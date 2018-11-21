package com.mi.hundsun.oxchains.consumer.admin.controller.exchange;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.core.po.exchange.Exchange;
import com.mi.hundsun.oxchains.base.core.po.quote.Commodity;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.CommodityInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.ExchangeInterface;
import com.xiaoleilu.hutool.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class CodePairConfigController extends BaseController {

    @Autowired
    ExchangeInterface exchangeInterface;
    @Autowired
    CommodityInterface commodityInterface;

    @RequestMapping("codePairConfig/list")
    public String list(ModelMap modelMap) throws Exception {
        List<Commodity>  commoditys = commodityInterface.select(new Commodity(c->{
            c.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        List<Exchange> exchangeList = exchangeInterface.select(new Exchange(t -> {
            t.setState(Exchange.STATE.ENABLE.code);
            t.setDelFlag(Exchange.DELFLAG.NO.code);
        }));
        StringBuffer exchangeNums = new StringBuffer();
        if (CollectionUtil.isNotEmpty(exchangeList)) {
            for (Exchange ex : exchangeList) {
                exchangeNums.append(ex.getExNo()).append(",");
            }
        }
        modelMap.addAttribute("commoditys",commoditys);
        modelMap.addAttribute("exchanges", exchangeList);
        modelMap.addAttribute("exchangeNums", exchangeNums.substring(0,exchangeNums.length()));
        return ok("exchange/codePairConfig/list");
    }
}
