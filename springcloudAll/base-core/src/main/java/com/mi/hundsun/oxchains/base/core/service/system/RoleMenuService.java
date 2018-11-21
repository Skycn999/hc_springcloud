package com.mi.hundsun.oxchains.base.core.service.system;

import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import com.mi.hundsun.oxchains.base.core.po.system.RoleMenu;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.util.List;
import java.util.Set;

/**
 * 操作权限业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: RoleMenuService
 * @date 2017-08-15 03:14:49
 */
public interface RoleMenuService extends GenericService<Integer, RoleMenu> {

    /**
     * 保存角色菜单关系
     *
     * @param roleMenu
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    RoleMenu saveRoleMenu(RoleMenu roleMenu) throws BussinessException;

    /**
     * 删除角色菜单关系，实际上做逻辑删除操作
     *
     * @param roleMenu
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    void deleteRoleMenu(RoleMenu roleMenu) throws BussinessException;

    /**
     * 修改角色菜单关系
     *
     * @param roleMenu
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    void updateRoleMenu(RoleMenu roleMenu) throws BussinessException;


    /**
     * 删除角色菜单关系，实际上做逻辑删除操作
     *
     * @param id 主键
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    RoleMenu getRoleMenuById(Integer id) throws BussinessException;

    /**
     * 根据角色ID查询该角色权限列表
     *
     * @param roleId 角色ID
     * @return
     */
    Set<String> findPermissionByRoleId(Integer roleId);


    /**
     * 根据角色ID 查询所有菜单ID
     *
     * @param roleId 角色ID
     * @return
     */
    List<Integer> findMenuIdsByRoleId(int roleId);


    /**
     * 根据角色ID 查询所有菜单
     *
     * @param roleId 角色ID
     * @return
     */
    List<Menu> findMenusByRoleId(int roleId);
}
