package com.mi.hundsun.oxchains.consumer.admin.controller.tx;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 主委托管理controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class MainDelegationController extends BaseController {

    /**
     * 主委托管理列表
     * @param modelMap
     * @return
     */
    @RequestMapping("mainDelegation/list")
    public String list(ModelMap modelMap) {
        return ok("tx/mainDelegation/list");
    }

}
