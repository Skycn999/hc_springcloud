package com.mi.hundsun.oxchains.consumer.admin.controller.cms;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class BannerController extends BaseController {

    /**
     * 公告列表
     * @param modelMap
     * @return
     */
    //userIdentify/list
    @RequestMapping("banner/list")
    public String list(ModelMap modelMap) {
        return ok("cms/banner/list");
    }
}
