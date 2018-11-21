package com.mi.hundsun.oxchains.consumer.admin.service.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import com.mi.hundsun.oxchains.base.core.po.system.RoleMenu;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 操作权限业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: RoleMenuInterface
 * @date 2017-08-15 03:14:49
 */
@FeignClient("provider-admin-${feignSuffix}")
public interface RoleMenuInterface {

    /**
     * 保存角色菜单关系
     *
     * @param roleMenu
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/sys/rolemenu/saveRoleMenu")
    RoleMenu saveRoleMenu(@RequestBody RoleMenu roleMenu) throws BussinessException;

    /**
     * 删除角色菜单关系，实际上做逻辑删除操作
     *
     * @param roleMenu
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/sys/rolemenu/deleteRoleMenu")
    void deleteRoleMenu(@RequestBody RoleMenu roleMenu) throws BussinessException;

    /**
     * 修改角色菜单关系
     *
     * @param roleMenu
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/sys/rolemenu/updateRoleMenu")
    void updateRoleMenu(@RequestBody RoleMenu roleMenu) throws BussinessException;


    /**
     * 删除角色菜单关系，实际上做逻辑删除操作
     *
     * @param id 主键
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/sys/rolemenu/getRoleMenuById")
    RoleMenu getRoleMenuById(@RequestParam("id") Integer id) throws BussinessException;

    /**
     * 根据角色ID查询该角色权限列表
     *
     * @param roleId 角色ID
     * @return
     */
    @PostMapping(value = "/sys/rolemenu/findPermissionByRoleId")
    Set<String> findPermissionByRoleId(@RequestParam("roleId") Integer roleId);


    /**
     * 根据角色ID 查询所有菜单ID
     *
     * @param roleId 角色ID
     * @return
     */
    @PostMapping(value = "/sys/rolemenu/findMenuIdsByRoleId")
    List<Integer> findMenuIdsByRoleId(@RequestParam("roleId") int roleId);


    /**
     * 根据角色ID 查询所有菜单
     *
     * @param roleId 角色ID
     * @return
     */
    @PostMapping(value = "/sys/rolemenu/findMenusByRoleId")
    List<Menu> findMenusByRoleId(@RequestParam("roleId") int roleId);

    @PostMapping(value = "/sys/rolemenu/selectOne")
    RoleMenu selectOne(@RequestBody RoleMenu rolemenu);

    @PostMapping(value = "/sys/rolemenu/select")
    List<RoleMenu> select(@RequestBody RoleMenu rolemenu);

    @PostMapping(value = "/sys/rolemenu/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager);

    @PostMapping(value = "/sys/rolemenu/insert")
    void insert(@RequestBody RoleMenu rolemenu);

    @PostMapping(value = "/sys/rolemenu/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody RoleMenu rolemenu);

    @PostMapping(value = "/sys/rolemenu/selectAll")
    List<RoleMenu> selectAll();
}
