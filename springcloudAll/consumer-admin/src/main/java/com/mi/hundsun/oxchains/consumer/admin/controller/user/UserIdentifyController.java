package com.mi.hundsun.oxchains.consumer.admin.controller.user;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(BaseController.BASE_URI)
public class UserIdentifyController extends BaseController{

    /**
     * 用户认证列表
     * @param modelMap
     * @return
     */
    //userIdentify/list
    @RequestMapping("userIdentify/list")
    public String list(ModelMap modelMap) {
        return ok("user/userIdentify/list");
    }
}
