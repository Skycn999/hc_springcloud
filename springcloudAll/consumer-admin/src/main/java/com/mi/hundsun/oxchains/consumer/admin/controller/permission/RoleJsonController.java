package com.mi.hundsun.oxchains.consumer.admin.controller.permission;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.system.RoleInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.system.RoleMenuInterface;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.JsonUtils;
import com.mi.hundsun.oxchains.base.core.po.system.Role;
import com.mi.hundsun.oxchains.base.core.service.system.RoleMenuService;
import com.mi.hundsun.oxchains.base.core.service.system.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 后台管理员组
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class RoleJsonController extends GenericController<Integer,Role> {


    @Autowired
    RoleMenuInterface roleMenuInterface;

    @Autowired
    RoleInterface roleInterface;

    /**
     * 管理员组列表JSON
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping("/role/json/list")
    @RequiresPermissions("sys:role:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return roleInterface.getDtGridList(dtGridPager);
    }

    /**
     * 角色信息保存或更新
     *
     * @param id
     * @param name
     * @param permission
     * @return
     */
    @ResponseBody
    @RequestMapping("/role/json/save")
    @RequiresPermissions("sys:role:save")
    public ResultEntity addJson(@RequestParam(value = "id", defaultValue = "0") int id,
                                @RequestParam String name,
                                @RequestParam String permission) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        List<Integer> menuIdList = JsonUtils.toGenericObject(permission, new TypeReference<List<Integer>>() {});
        roleInterface.saveOrUpdateRoleMenu(id, name, menuIdList);
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(BaseController.BASE_URI+"/role/list");
        return resultEntity;
    }

    /**
     * 管理员组删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/role/json/del")
    @RequiresPermissions("sys:role:del")
    public ResultEntity delJson(@RequestParam int id) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        roleInterface.deleteRoleById(id);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }
}