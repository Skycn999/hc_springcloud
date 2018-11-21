package com.mi.hundsun.oxchains.base.core.mapper.system;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import com.mi.hundsun.oxchains.base.core.po.system.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作权限相关Dao接口<br>
 *
 * @author donfy
 * @date 2017-08-15 03:14:49
 */
public interface RoleMenuMapper extends GenericMapper<RoleMenu, Integer> {


    @Select("SELECT m.permission " +
            " FROM s_role_menu rm " +
            " LEFT JOIN s_menu m on m.id = rm.menu_id " +
            " WHERE rm.role_id = #{roleId}")
    List<String> findPermisByRoleId(Integer roleId);

    /**
     * 根据角色ID 删除角色菜单关联关系
     *
     * @param roleId
     */
    @Delete("delete from s_role_menu where role_id = #{roleId}")
    void deleteByRoleId(Integer roleId);



    /**
     * 根据角色ID 删除角色菜单关联关系
     *
     * @param roleId
     */
    @Select("SELECT m.* FROM s_menu m LEFT JOIN  s_role_menu rm on rm.menu_id = m.id " +
            "WHERE rm.role_id = #{roleId} AND  m.del_flag = 1 " +
            "ORDER BY  sort_no ASC,id ASC")
    List<Menu> findMenusByRoleId(@Param(value = "roleId") Integer roleId);

}
