package com.mi.hundsun.oxchains.consumer.admin.controller.tpl;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class ProtocolController extends BaseController{

    /**
     * 协议列表
     * @param modelMap
     * @return
     */
    //userIdentify/list
    @RequestMapping("protocol/list")
    public String list(ModelMap modelMap) {
        return ok("tpl/protocol/list");
    }
}
