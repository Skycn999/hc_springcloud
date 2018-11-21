package com.mi.hundsun.oxchains.consumer.admin.controller.setting;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 消息模板Controller
 *
 * @author liweidong
 * @date 2017年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class MsgTemplateController extends BaseController {


    /**
     * 消息模板列表
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/msgTemplate/list")
    public String list(ModelMap modelMap) {
        return ok("setting/msgTemplate/list");
    }
}
