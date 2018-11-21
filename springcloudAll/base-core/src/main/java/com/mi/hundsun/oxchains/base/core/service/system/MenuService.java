package com.mi.hundsun.oxchains.base.core.service.system;

import com.mi.hundsun.oxchains.base.core.model.system.MenuModel;
import com.mi.hundsun.oxchains.base.core.model.system.MenuStateModel;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.util.List;

/**
 * 系统菜单业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: MenuService
 * @date 2017-08-14 07:53:50
 */
public interface MenuService extends GenericService<Integer, Menu> {


//    /**
//     * 保存后台菜单
//     *
//     * @param menu
//     * @return void    返回类型
//     * @throws BussinessException 异常信息
//     */
//    Menu saveMenu(Menu menu) throws BussinessException;
//
//    /**
//     * 删除后台菜单，实际上做逻辑删除操作
//     *
//     * @param menu
//     * @return void    返回类型
//     * @throws BussinessException 异常信息
//     */
//    void deleteMenu(Menu menu) throws BussinessException;
//
//    /**
//     * 修改后台菜单
//     *
//     * @param menu
//     * @return void    返回类型
//     * @throws BussinessException 异常信息
//     */
//    void updateMenu(Menu menu) throws BussinessException;
//
//
//    /**
//     * 删除后台菜单，实际上做逻辑删除操作
//     *
//     * @param id 主键
//     * @return void    返回类型
//     * @throws BussinessException 异常信息
//     */
//    Menu getMenuById(Integer id) throws BussinessException;

    /**
     * 根据角色ID 和 父级菜单ID查询菜单树
     *
     * @param roleId   角色ID
     * @param parentId 父级菜单ID
     * @return
     */
    List<MenuModel> findPermiMenuList(Integer roleId, Integer parentId);

    /**
     * 查询后台菜单选中状态
     *
     * @param path
     * @return
     */
    MenuStateModel getMenuState(String path);

    /**
     * 根据ID 删除菜单及其所有子菜单
     *
     * @param id
     */
    void delMenuInculdeSuns(Integer id);
}
