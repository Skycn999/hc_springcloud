package com.mi.hundsun.oxchains.consumer.admin.controller.tpl;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class TradeFeeController extends BaseController {

    /**
     * 交易手续费率模版列表
     * @param modelMap
     * @return
     */
    @RequestMapping("tradeFee/list")
    public String list(ModelMap modelMap) {
        return ok("tpl/tradeFee/list");
    }

}
