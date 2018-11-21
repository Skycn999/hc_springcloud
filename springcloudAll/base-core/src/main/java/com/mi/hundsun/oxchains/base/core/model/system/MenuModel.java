package com.mi.hundsun.oxchains.base.core.model.system;


import com.mi.hundsun.oxchains.base.core.po.system.Menu;

import java.util.List;

/**
 * 菜单树结构Model
 *
 * @author Vector
 * @create 2017-06-06 20:16
 */
public class MenuModel extends Menu {
    /**
     * 子菜单列表
     */
    private List<MenuModel> subMenu;

    public List<MenuModel> getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(List<MenuModel> subMenu) {
        this.subMenu = subMenu;
    }
}
