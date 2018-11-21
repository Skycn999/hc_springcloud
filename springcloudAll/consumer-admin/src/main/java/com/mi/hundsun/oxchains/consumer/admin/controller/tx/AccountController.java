package com.mi.hundsun.oxchains.consumer.admin.controller.tx;


import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户资产持仓controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class AccountController extends BaseController {

    /**
     * 用户资产持仓列表
     * @param modelMap
     * @return
     */
    @RequestMapping("account/list")
    public String list(ModelMap modelMap) {
        return ok("tx/account/list");
    }

}
