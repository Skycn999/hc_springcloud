package com.mi.hundsun.oxchains.consumer.admin.controller.user;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class UserAddressController extends BaseController{

    /**
     * 用户地址列表
     * @param modelMap
     * @return
     */
    //userIdentify/list
    @RequestMapping("userAddress/list")
    public String list(ModelMap modelMap) {
        return ok("user/userAddress/list");
    }
}
