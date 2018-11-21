package com.mi.hundsun.oxchains.provider.admin.controller.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Role;
import com.mi.hundsun.oxchains.base.core.service.system.RoleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统角色表业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: RoleController
 * @date 2017-08-21 09:50:59
 */
@Slf4j
@RestController
@RequestMapping("/sys/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    /**
     * 保存后台管理员角色
     *
     * @param role
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/saveRole")
    public Role saveRole(@RequestBody Role role) throws BussinessException {
        return roleService.saveRole(role);
    }

    /**
     * 删除后台管理员角色，实际上做逻辑删除操作
     *
     * @param role
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/deleteRole")
    public void deleteRole(@RequestBody Role role) throws BussinessException {
        roleService.deleteRole(role);
    }

    /**
     * 修改后台管理员角色
     *
     * @param role
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "e/updateRole")
    public void updateRole(@RequestBody Role role) throws BussinessException {
        roleService.updateRole(role);
    }


    /**
     * 删除后台管理员角色，实际上做逻辑删除操作
     *
     * @param id 主键
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "e/getRoleById")
    public Role getRoleById(Integer id) throws BussinessException {
        return roleService.getRoleById(id);
    }

    /**
     * 角色信息保存或更新
     *
     * @param id
     * @param name
     * @param menuIdList
     */
    @PostMapping(value = "/saveOrUpdateRoleMenu")
    public void saveOrUpdateRoleMenu(Integer id, String name, @RequestBody List<Integer> menuIdList) {
        roleService.saveOrUpdateRoleMenu(id, name, menuIdList);
    }

    /**
     * 删除角色信息以及角色菜单关联关系
     *
     * @param id
     */
    @PostMapping(value = "/sys/role/deleteRoleById")
    public void deleteRoleById(Integer id){
        roleService.deleteRoleById(id);
    }


    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insert(@RequestBody Role role) throws BussinessException {
        roleService.insert(role);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Role role) throws BussinessException {
        roleService.updateByPrimaryKeySelective(role);
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        roleService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/removeById")
    public void removeById(@RequestBody Role role) throws BussinessException {
        roleService.removeById(role);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/getNormalModelById")
    public Role getNormalModelById(@RequestBody Role role) throws BussinessException {
        return roleService.getNormalModelById(role);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/selectOne")
    public Role selectOne(@RequestBody Role role) throws BussinessException {
        return roleService.selectOne(role);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<Role> select(@RequestBody Role role) throws BussinessException {
        return roleService.select(role);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/selectAll")
    public List<Role> selectAll() throws BussinessException {
        return roleService.selectAll();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return roleService.getDtGridList(dtGridPager);
    }
}
