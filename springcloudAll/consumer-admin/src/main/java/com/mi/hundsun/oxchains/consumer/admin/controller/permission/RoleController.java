package com.mi.hundsun.oxchains.consumer.admin.controller.permission;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.system.MenuInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.system.RoleInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.system.RoleMenuInterface;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import com.mi.hundsun.oxchains.base.core.po.system.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台管理员组
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class RoleController extends BaseController {

    @Autowired
    RoleMenuInterface roleMenuInterface;

    @Autowired
    RoleInterface roleInterface;

    @Autowired
    MenuInterface menuInterface;

    public RoleController() {
        ok("role/list");
    }

    /**
     * 管理员组列表
     *
     * @return
     */
    @RequestMapping("/role/list")
    public String list() {
        return getAdminTemplate("admin/role/list");
    }

    /**
     * 管理员组添加
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/role/add")
    public String add(ModelMap modelMap) {
        List<Menu> list = menuInterface.selectAll();
        modelMap.addAttribute("menuList", list);
        return getAdminTemplate("admin/role/add");
    }

    /**
     * 管理员组编辑
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping("/role/edit")
    public String edit(@RequestParam int id, ModelMap modelMap) {
        setMenuPath("role/list");

        Role role = roleInterface.getNormalModelById(new Role(r -> r.setId(id)));
        modelMap.addAttribute("role", role);

        List<Menu> list = menuInterface.selectAll();
        modelMap.addAttribute("menuList", list);

        return getAdminTemplate("admin/role/add");
    }

}