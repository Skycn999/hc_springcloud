package com.mi.hundsun.oxchains.consumer.admin.controller.tpl;

import com.mi.hundsun.oxchains.base.core.po.quote.Commodity;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.CommodityInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.tpl.ServiceFeeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 手续费模板controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class ServiceFeeController extends BaseController {

    /**
     * 手续费模板列表
     * @param modelMap
     * @return
     */
    @RequestMapping("serviceFee/list")
    public String list(ModelMap modelMap) {
        return ok("tpl/serviceFee/list");
    }

}
