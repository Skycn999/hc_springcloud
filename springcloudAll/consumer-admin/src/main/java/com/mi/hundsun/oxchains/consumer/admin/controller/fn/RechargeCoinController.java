package com.mi.hundsun.oxchains.consumer.admin.controller.fn;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 充币管理controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class RechargeCoinController extends BaseController {

    @RequestMapping("rechargeCoin/list")
    public String list(ModelMap modelMap) {
        return ok("fn/rechargeCoin/list");
    }
}
