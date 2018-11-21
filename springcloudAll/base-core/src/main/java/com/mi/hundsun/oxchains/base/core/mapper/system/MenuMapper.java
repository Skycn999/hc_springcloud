package com.mi.hundsun.oxchains.base.core.mapper.system;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.system.MenuModel;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 系统菜单相关Dao接口<br>
 *
 * @author donfy
 * @date 2017-08-14 07:53:50
 */
public interface MenuMapper extends GenericMapper<Menu, Integer> {


    /**
     * 查询用户权限菜单
     *
     * @param params
     * @return
     */
    @Select("SELECT m.* FROM s_menu m LEFT JOIN  s_role_menu rm on rm.menu_id = m.id " +
            "WHERE rm.role_id = #{roleId} AND m.parent_id = #{parentId} AND m.type != 2 AND  m.del_flag = 0 " +
            "ORDER BY  sort_no ASC,id ASC")
    List<MenuModel> findPermiMenuList(Map<String, Object> params);

    /**
     * 查询所有顶级菜单
     *
     * @param parentId 父级菜单ID
     * @return
     */
    @Select("SELECT * FROM s_menu " +
            "WHERE del_flag = 1 AND `type` != 2 AND parent_id = #{parentId} " +
            "ORDER BY  sort_no ASC,id ASC")
    List<MenuModel> findSubMenuList(Integer parentId);

    /**
     * 根据父级ID 列表查询所有子级菜单
     *
     * @param parentIds
     * @return
     */
    @Select("<script>" +
            "select id from s_menu where parent_id in" +
            "<foreach collection='parentIds' item='item' index='index' open='(' close=')' separator=','>" +
            " #{item} " +
            " </foreach>" +
            "</script>")
    List<Integer> findSunByParentList(@Param("parentIds") List<Integer> parentIds);

    /**
     * 根据菜单ID列表删除菜单
     *
     * @param ids
     */
    @Delete("<script>" + "delete from s_menu where id in " +
            "<foreach collection='ids' item='id' index='index' open='(' close=')' separator=','> " +
            "#{id} " +
            "</foreach>" +
            "</script>")
    void deleteByIds(@Param("ids") List<Integer> ids);

}
