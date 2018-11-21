package com.mi.hundsun.oxchains.consumer.admin.controller.setting;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台参数配置
 *
 * @author liweidong
 * @date 2017年03月10日 15:28
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class ConfigureController extends BaseController {


    /**
     * 参数配置列表
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/config/list")
    public String list(ModelMap modelMap) {
        return ok("setting/config/list");
    }

}