package com.mi.hundsun.oxchains.consumer.admin.controller.tx;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 成交管理controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class DealOrderController extends BaseController {

    /**
     * 主委托管理列表
     * @param modelMap
     * @return
     */
    @RequestMapping("dealOrder/list")
    public String list(ModelMap modelMap) {
        return ok("tx/dealOrder/list");
    }

}
