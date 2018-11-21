package com.mi.hundsun.oxchains.base.core.service.system.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.mapper.system.MenuMapper;
import com.mi.hundsun.oxchains.base.core.model.system.MenuModel;
import com.mi.hundsun.oxchains.base.core.model.system.MenuStateModel;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import com.mi.hundsun.oxchains.base.core.service.system.MenuService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 系统菜单业务相关Service接口实现<br>
 *
 * @author donfy
 * @ClassName: MenuServiceImpl
 * @date 2017-08-14 07:53:50
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public GenericMapper< Menu,Integer> _getMapper() {
        return menuMapper;
    }

//    @Override
//    public Menu saveMenu(Menu menu) throws BussinessException {
//        return this.saveModel(menu);
//    }
//
//    @Override
//    public void deleteMenu(Menu menu) throws BussinessException {
//        menu.setDelFlag(Menu.DELFLAG.DELETED.code);
//        this.updateModelBySelectiveAndId(menu);
//    }
//
//    @Override
//    public void updateMenu(Menu menu) throws BussinessException {
//        this.updateModelBySelectiveAndId(menu);
//    }
//
//    @Override
//    public Menu getMenuById(Integer id) throws BussinessException {
//        return this.getModelById(id);
//    }

    @Override
    public List<MenuModel> findPermiMenuList(Integer roleId, Integer parentId) {
        List<MenuModel> topList;
        if (roleId == 0) { // 超级管理员，拥有所有权限
            // 查询顶级菜单
            topList = menuMapper.findSubMenuList(parentId);
            for (MenuModel topMenu : topList) {
                // 查询1级菜单
                List<MenuModel> menuList1 = menuMapper.findSubMenuList(topMenu.getId());
                for (MenuModel menu1 : menuList1) {
                    // 查询2级菜单
                    List<MenuModel> menuList2 = menuMapper.findSubMenuList(menu1.getId());
                    for (MenuModel menu2 : menuList2) {
                        // 查询3级菜单
                        List<MenuModel> menuList3 = menuMapper.findSubMenuList(menu2.getId());
                        menu2.setSubMenu(menuList3);
                    }
                    menu1.setSubMenu(menuList2);
                }
                topMenu.setSubMenu(menuList1);
            }
        } else { // 非超级管理员，按照角色查询菜单
            // 查询顶级菜单
            Map<String, Object> params = new HashMap<>();
            params.put("roleId", roleId);
            params.put("parentId", parentId);
            topList = menuMapper.findPermiMenuList(params);
            for (MenuModel topMenu : topList) {
                // 查询1级菜单
                params.put("parentId", topMenu.getId());
                List<MenuModel> menuList1 = menuMapper.findPermiMenuList(params);
                for (MenuModel menu1 : menuList1) {
                    // 查询2级菜单
                    params.put("parentId", menu1.getId());
                    List<MenuModel> menuList2 = menuMapper.findPermiMenuList(params);
                    for (MenuModel menu2 : menuList2) {
                        // 查询3级菜单
                        params.put("parentId", menu2.getId());
                        List<MenuModel> menuList3 = menuMapper.findPermiMenuList(params);
                        menu2.setSubMenu(menuList3);
                    }
                    menu1.setSubMenu(menuList2);
                }
                topMenu.setSubMenu(menuList1);
            }
        }
        return topList;
    }

    @Override
    public MenuStateModel getMenuState(String path) {
        if (path.equals("")) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            path = request.getServletPath();
            if (path == null || path.equals("")) {
                path = "/";
            }
            path = path.substring(1, path.length());
        }

        MenuStateModel menuState = new MenuStateModel();
        List<String> breadCrumbList = new LinkedList<String>();

        List<Menu> adminMenuList = menuMapper.selectAll();

        Menu menu1 = null;
        Menu menu2 = null;
        List<Menu> menuTree = new ArrayList<Menu>();
        for (Menu menu : adminMenuList) {
            if (menu.getUrl() != null && menu.getUrl().trim().equals(path)) {
                menu1 = menu;
                menuTree.add(menu);
                break;
            }
        }

        if (menu1 != null && menu1.getParentId() > 0) {
            for (Menu menu : adminMenuList) {
                if (menu1.getParentId().equals(menu.getId())) {
                    menu2 = menu;
                    menuTree.add(menu);
                    break;
                }
            }
        }

        if (menu2 != null && menu2.getParentId() > 0) {
            for (Menu menu : adminMenuList) {
                if (menu2.getParentId().equals(menu.getId())) {
                    menuTree.add(menu);
                    break;
                }
            }
        }

        int count = menuTree.size();
        if (count == 3) {
            menuState.setCurrentMenu1(menuTree.get(2).getId());
            menuState.setCurrentMenu2(menuTree.get(1).getId());
            menuState.setCurrentMenu3(menuTree.get(0).getId());
        }

        if (count == 2) {
            menuState.setCurrentMenu1(menuTree.get(1).getId());
            menuState.setCurrentMenu2(menuTree.get(0).getId());
        }

        if (count == 1) {
            menuState.setCurrentMenu1(menuTree.get(0).getId());
        }

        for (int i = count; i > 0; i--) {
            breadCrumbList.add(menuTree.get(i - 1).getName());
        }

        menuState.setBreadCrumbList(breadCrumbList);

        return menuState;
    }

    @Override
    public void delMenuInculdeSuns(Integer id) {
        // 待删除菜单ID列表
        List<Integer> delIds = new ArrayList<>();
        delIds.add(id);

        // 查询子菜单的ID列表参数
        List<Integer> params = new ArrayList<>();
        params.add(id);

        // 查询所有自菜单ID
        while (!params.isEmpty()) {
            List<Integer> sunIds = menuMapper.findSunByParentList(params);
            params.clear();
            if (sunIds != null) {
                params.addAll(sunIds);
                for (Integer menuId : sunIds) {
                    delIds.add(menuId);
                }
            }
        }

        // 删除所有菜单
        menuMapper.deleteByIds(delIds);
    }

}
