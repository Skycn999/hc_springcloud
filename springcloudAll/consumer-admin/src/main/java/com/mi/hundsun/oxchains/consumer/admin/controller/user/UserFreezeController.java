package com.mi.hundsun.oxchains.consumer.admin.controller.user;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户冻结
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class UserFreezeController extends BaseController {

    /**
     * 用户冻结列表
     * @param modelMap
     * @return
     */
    @RequestMapping("userFreeze/list")
    public String list(ModelMap modelMap) {
        return ok("user/userFreeze/list");
    }
}
