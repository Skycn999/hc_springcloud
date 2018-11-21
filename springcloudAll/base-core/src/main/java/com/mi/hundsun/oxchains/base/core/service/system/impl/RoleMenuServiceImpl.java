package com.mi.hundsun.oxchains.base.core.service.system.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.mapper.system.MenuMapper;
import com.mi.hundsun.oxchains.base.core.mapper.system.RoleMenuMapper;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import com.mi.hundsun.oxchains.base.core.po.system.RoleMenu;
import com.mi.hundsun.oxchains.base.core.service.system.RoleMenuService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 操作权限业务相关Service接口实现<br>
 *
 * @author donfy
 * @ClassName: RoleMenuServiceImpl
 * @date 2017-08-15 03:14:49
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class RoleMenuServiceImpl implements RoleMenuService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuMapper menuMapper;

    @Override
    public GenericMapper<RoleMenu, Integer> _getMapper() {
        return roleMenuMapper;
    }


    @Override
    public Set<String> findPermissionByRoleId(Integer roleId) {
        if (roleId == null) {
            return null;
        }
        List<String> permissions = new ArrayList<>();
        if (roleId == 0) { // 超级管理员权限，拥有所有权限
            Menu model = new Menu();
            model.setDelFlag(Menu.DELFLAG.NO.code);
            List<Menu> menus = menuMapper.select(model);
            for (Menu menu : menus) {
                if (menu.getPermission() != null && !menu.getPermission().trim().equals("")) {
                    permissions.add(menu.getPermission());
                }
            }
        } else {// 非超级管理员，根据角色ID 查询拥有的权限
            List<String> permiTemps = roleMenuMapper.findPermisByRoleId(roleId);
            for (String permi : permiTemps) {
                if (StringUtils.isNotBlank(permi)) {
                    permissions.add(permi);
                }
            }
        }
        Set<String> permissionSet = new HashSet<String>(permissions);
        return permissionSet;
    }

    @Override
    public RoleMenu saveRoleMenu(RoleMenu roleMenu) throws BussinessException {
        roleMenu.setDelFlag(RoleMenu.DELFLAG.NO.code);
        roleMenu.setCreateTime(new Date());
        roleMenuMapper.insert(roleMenu);
        return roleMenu;
    }

    @Override
    public void deleteRoleMenu(RoleMenu roleMenu) throws BussinessException {
        roleMenu.setDelFlag(RoleMenu.DELFLAG.YES.code);
        roleMenuMapper.updateByPrimaryKeySelective(roleMenu);
    }

    @Override
    public void updateRoleMenu(RoleMenu roleMenu) throws BussinessException {
        roleMenuMapper.updateByPrimaryKeySelective(roleMenu);
    }

    @Override
    public RoleMenu getRoleMenuById(Integer id) throws BussinessException {
        return roleMenuMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Integer> findMenuIdsByRoleId(int roleId) {
        List<Integer> menuIds = new ArrayList<>();
        RoleMenu params = new RoleMenu();
        params.setRoleId(roleId);
        List<RoleMenu> list = select(params);
        for (RoleMenu roleMenu : list) {
            menuIds.add(roleMenu.getMenuId());
        }
        return menuIds;
    }

    @Override
    public List<Menu> findMenusByRoleId(int roleId) {
        return roleMenuMapper.findMenusByRoleId(roleId);
    }

}
