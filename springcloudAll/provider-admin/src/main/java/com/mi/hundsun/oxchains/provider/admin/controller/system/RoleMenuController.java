package com.mi.hundsun.oxchains.provider.admin.controller.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import com.mi.hundsun.oxchains.base.core.po.system.RoleMenu;
import com.mi.hundsun.oxchains.base.core.service.system.RoleMenuService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 操作权限业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: RoleMenuController
 * @date 2017-08-15 03:14:49
 */
@Slf4j
@RestController
@RequestMapping("/sys/rolemenu")
public class RoleMenuController{

    @Autowired
    RoleMenuService roleMenuService;
    /**
     * 保存角色菜单关系
     *
     * @param roleMenu
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/saveRoleMenu")
    public RoleMenu saveRoleMenu(@RequestBody RoleMenu roleMenu) throws BussinessException{
        return roleMenuService.saveRoleMenu(roleMenu);
    }

    /**
     * 删除角色菜单关系，实际上做逻辑删除操作
     *
     * @param roleMenu
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/deleteRoleMenu")
    public void deleteRoleMenu(@RequestBody RoleMenu roleMenu) throws BussinessException{
        roleMenuService.deleteRoleMenu(roleMenu);
    }

    /**
     * 修改角色菜单关系
     *
     * @param roleMenu
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/updateRoleMenu")
    public void updateRoleMenu(@RequestBody RoleMenu roleMenu) throws BussinessException{
        roleMenuService.updateRoleMenu(roleMenu);
    }


    /**
     * 删除角色菜单关系，实际上做逻辑删除操作
     *
     * @param id 主键
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/getRoleMenuById")
    public RoleMenu getRoleMenuById(Integer id) throws BussinessException{
        return roleMenuService.getRoleMenuById(id);
    }

    /**
     * 根据角色ID查询该角色权限列表
     *
     * @param roleId 角色ID
     * @return
     */
    @PostMapping(value = "findPermissionByRoleId")
    public Set<String> findPermissionByRoleId(Integer roleId){
        return roleMenuService.findPermissionByRoleId(roleId);
    }


    /**
     * 根据角色ID 查询所有菜单ID
     *
     * @param roleId 角色ID
     * @return
     */
    @PostMapping(value = "/findMenuIdsByRoleId")
    public List<Integer> findMenuIdsByRoleId(int roleId){
        return roleMenuService.findMenuIdsByRoleId(roleId);
    }


    /**
     * 根据角色ID 查询所有菜单
     *
     * @param roleId 角色ID
     * @return
     */
    @PostMapping(value = "/findMenusByRoleId")
    public List<Menu> findMenusByRoleId(int roleId){
        return roleMenuService.findMenusByRoleId(roleId);
    }


    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insert(@RequestBody RoleMenu roleMenu) throws BussinessException {
        roleMenuService.insert(roleMenu);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody RoleMenu roleMenu) throws BussinessException {
        roleMenuService.updateByPrimaryKeySelective(roleMenu);
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        roleMenuService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/removeById")
    public void removeById(@RequestBody RoleMenu roleMenu) throws BussinessException {
        roleMenuService.removeById(roleMenu);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/getNormalModelById")
    public RoleMenu getNormalModelById(@RequestBody RoleMenu roleMenu) throws BussinessException {
        return roleMenuService.getNormalModelById(roleMenu);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/selectOne")
    public RoleMenu selectOne(@RequestBody RoleMenu roleMenu) throws BussinessException {
        return roleMenuService.selectOne(roleMenu);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<RoleMenu> select(@RequestBody RoleMenu roleMenu) throws BussinessException {
        return roleMenuService.select(roleMenu);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/selectAll")
    public List<RoleMenu> selectAll() throws BussinessException {
        return roleMenuService.selectAll();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return roleMenuService.getDtGridList(dtGridPager);
    }
}
