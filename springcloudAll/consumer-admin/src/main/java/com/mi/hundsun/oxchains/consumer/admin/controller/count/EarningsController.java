package com.mi.hundsun.oxchains.consumer.admin.controller.count;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class EarningsController extends BaseController{

    /**
     * 收益汇总列表
     * @param modelMap
     * @return
     */
    @RequestMapping("earnings/list")
    public String list(ModelMap modelMap) {
        return ok("count/earnings/list");
    }
}
