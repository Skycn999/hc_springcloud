package com.mi.hundsun.oxchains.consumer.admin.controller.permission;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.system.RoleInterface;
import com.mi.hundsun.oxchains.base.core.po.system.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 后台管理员
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class AdminController extends BaseController {

    @Autowired
    RoleInterface roleInterface;

    /**
     * 管理员列表
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("admin/list")
    public String list(ModelMap modelMap) {
        List<Role> list = roleInterface.selectAll();
        modelMap.addAttribute("roleList", list);
        return getAdminTemplate("admin/admin/list");
    }
}