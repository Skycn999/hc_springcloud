package com.mi.hundsun.oxchains.consumer.admin.service.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Role;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 系统角色表业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: RoleInterface
 * @date 2017-08-21 09:50:59
 */
@FeignClient("provider-admin-${feignSuffix}")
public interface RoleInterface{

    /**
     * 保存后台管理员角色
     *
     * @param role
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/sys/role/saveRole")
    Role saveRole(@RequestBody Role role) throws BussinessException;

    /**
     * 删除后台管理员角色，实际上做逻辑删除操作
     *
     * @param role
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/sys/role/deleteRole")
    void deleteRole(@RequestBody Role role) throws BussinessException;

    /**
     * 修改后台管理员角色
     *
     * @param role
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/sys/role/updateRole")
    void updateRole(@RequestBody Role role) throws BussinessException;


    /**
     * 删除后台管理员角色，实际上做逻辑删除操作
     *
     * @param id 主键
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/sys/role/getRoleById")
    Role getRoleById(@RequestParam("id") Integer id) throws BussinessException;

    /**
     * 角色信息保存或更新
     *
     * @param id
     * @param name
     * @param menuIdList
     */
    @PostMapping(value = "/sys/role/saveOrUpdateRoleMenu")
    void saveOrUpdateRoleMenu(@RequestParam("id") Integer id,
                              @RequestParam("name") String name,
                              @RequestBody List<Integer> menuIdList);

    /**
     * 删除角色信息以及角色菜单关联关系
     *
     * @param id
     */
    @PostMapping(value = "/sys/role/deleteRoleById")
    void deleteRoleById(@RequestParam("id") Integer id);


    @PostMapping(value = "/sys/role/selectOne")
    Role selectOne(@RequestBody Role role);

    @PostMapping(value = "/sys/role/select")
    List<Role> select(@RequestParam("bank") Role role);

    @PostMapping(value = "/sys/role/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager);

    @PostMapping(value = "/sys/role/insert")
    void insert(@RequestBody Role role);

    @PostMapping(value = "/sys/role/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Role role);

    @PostMapping(value = "/sys/role/selectAll")
    List<Role> selectAll();

    @PostMapping(value = "/sys/role/getNormalModelById")
    Role getNormalModelById(@RequestBody Role role);
}
