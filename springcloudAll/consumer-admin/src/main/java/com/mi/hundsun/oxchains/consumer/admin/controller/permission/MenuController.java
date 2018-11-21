package com.mi.hundsun.oxchains.consumer.admin.controller.permission;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.system.MenuInterface;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 菜单管理
 *
 * @author liweidong
 * @date 2017年03月05日 13:31
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class MenuController extends BaseController {

    @Autowired
    MenuInterface menuInterface;

    /**
     * 系统菜单列表
     * @param modelMap
     * @return
     */
    @RequestMapping("/menu/list")
    public String list(ModelMap modelMap) {
        List<Menu> list = menuInterface.select(new Menu());
        modelMap.addAttribute("menuList", list);
        return getAdminTemplate("admin/menu/list");
    }
}
