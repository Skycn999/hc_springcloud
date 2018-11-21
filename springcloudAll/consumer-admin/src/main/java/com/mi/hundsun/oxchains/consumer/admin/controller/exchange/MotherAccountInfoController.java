package com.mi.hundsun.oxchains.consumer.admin.controller.exchange;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 交易所母账号资产信息controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class MotherAccountInfoController extends BaseController {

    @RequestMapping("motherAccountInfo/list")
    public String list(ModelMap modelMap) throws Exception {
        return ok("exchange/motherAccountInfo/list");
    }
}
