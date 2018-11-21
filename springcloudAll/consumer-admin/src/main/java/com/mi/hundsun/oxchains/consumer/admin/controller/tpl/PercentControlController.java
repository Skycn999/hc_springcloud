package com.mi.hundsun.oxchains.consumer.admin.controller.tpl;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(BaseController.BASE_URI)
public class PercentControlController extends BaseController {

    /**
     * 百分比风控列表
     * @param modelMap
     * @return
     */
    //userIdentify/list
    @RequestMapping("percentControl/list")
    public String list(ModelMap modelMap) {
        return ok("tpl/percentControl/list");
    }
}
