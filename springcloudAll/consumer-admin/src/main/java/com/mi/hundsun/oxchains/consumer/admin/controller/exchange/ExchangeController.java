package com.mi.hundsun.oxchains.consumer.admin.controller.exchange;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 交易所管理controller
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class ExchangeController  extends BaseController{

    /**
     * 交易所管理列表
     * @param modelMap
     * @return
     */
    @RequestMapping("exchange/list")
    public String list(ModelMap modelMap) {
        return ok("exchange/exchange/list");
    }

}
