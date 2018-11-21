package com.mi.hundsun.oxchains.base.core.service.system;

import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Role;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.util.List;

/**
 * 系统角色表业务相关Service接口<br>
 *
 * @ClassName: RoleService
 * @author donfy
 * @date   2017-08-21 09:50:59
 */
public interface RoleService extends GenericService<Integer, Role> {

    /**
     * 保存后台管理员角色
     *
     * @param role
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    Role saveRole(Role role) throws BussinessException;

    /**
     * 删除后台管理员角色，实际上做逻辑删除操作
     *
     * @param role
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    void deleteRole(Role role) throws BussinessException;

    /**
     * 修改后台管理员角色
     *
     * @param role
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    void updateRole(Role role) throws BussinessException;


    /**
     * 删除后台管理员角色，实际上做逻辑删除操作
     *
     * @param id 主键
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    Role getRoleById(Integer id) throws BussinessException;

    /**
     * 角色信息保存或更新
     *
     * @param id
     * @param name
     * @param menuIdList
     */
    void saveOrUpdateRoleMenu(Integer id, String name, List<Integer> menuIdList);

    /**
     * 删除角色信息以及角色菜单关联关系
     *
     * @param id
     */
    void deleteRoleById(Integer id);
}
