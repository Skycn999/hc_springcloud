package com.mi.hundsun.oxchains.consumer.admin.controller.fn;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 平台地址管理controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class PlatUserAddressController extends BaseController {

    @RequestMapping("platUserAddress/list")
    public String list(ModelMap modelMap) {
        return ok("fn/platUserAddress/list");
    }

}
