package com.mi.hundsun.oxchains.consumer.admin.controller.permission;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.core.po.system.AdminRole;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.system.AdminRoleInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.system.MenuInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.system.RoleMenuInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * MenuJsonController
 *
 * @author liweidong
 * @date 2017年03月05日 13:49
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class MenuJsonController extends GenericController<Integer, Menu> {

    @Resource
    MenuInterface menuInterface;
    @Resource
    RoleMenuInterface roleMenuInterface;
    @Resource
    AdminRoleInterface adminRoleInterface;

    /**
     * 功能菜单列表json数据
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping("/menu/json/list")
    @RequiresPermissions("sys:menu:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return menuInterface.getDtGridList(dtGridPager);
    }

    /**
     * 添加功能菜单
     *
     * @param menu
     * @return
     */
    @ResponseBody
    @RequestMapping("/menu/json/add")
    @RequiresPermissions("sys:menu:save")
    public ResultEntity addJson(Menu menu) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        if (menu != null && menu.getParentId() != null && menu.getParentId() > 0) {
            Menu parent = menuInterface.selectOne(new Menu(m -> {
                m.setId(menu.getParentId());
                menu.setDelFlag(Menu.DELFLAG.NO.code);
            }));
            menu.setParentName(parent.getName());
        } else {
            menu.setParentName("");
            menu.setParentId(0);
        }
        menuInterface.insert(menu);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 编辑功能菜单
     *
     * @param menu
     * @return
     */
    @ResponseBody
    @RequestMapping("/menu/json/edit")
    @RequiresPermissions("sys:menu:edit")
    public ResultEntity editJson(Menu menu) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        menuInterface.updateByPrimaryKeySelective(menu);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 删除功能菜单（同时删除所有子菜单）
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/menu/json/del")
    @RequiresPermissions("sys:menu:del")
    public ResultEntity delJson(int id) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        menuInterface.delMenuInculdeSuns(id);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 获取所有菜单信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/menu/json/allMenu", method = RequestMethod.GET)
    public ResultEntity getAllMenuList() throws Exception {
        Admin admin = AdminSessionHelper.getCurrAdmin();
        List<AdminRole>  adminRoles = adminRoleInterface.select(new AdminRole(ar->{
            ar.setAdminId(admin.getId());
            ar.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if(adminRoles ==null ||adminRoles.size()<1 ){
            throw new Exception();
        }
        AdminRole adminRole = adminRoles.get(0);
        List<Menu> list;
        if (adminRole.getRoleId()==1){
            list = menuInterface.selectAll();
        }else {
            list = roleMenuInterface.findMenusByRoleId(adminRole.getRoleId());
        }
        return ok(list);
    }


    /**
     * 获取角色拥有的菜单ID列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/menu/json/roleMenuIds", method = RequestMethod.GET)
    public ResultEntity roleMenuIds(int roleId) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setData(roleMenuInterface.findMenuIdsByRoleId(roleId));
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }
}
