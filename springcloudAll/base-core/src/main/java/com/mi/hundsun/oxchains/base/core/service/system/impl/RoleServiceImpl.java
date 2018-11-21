package com.mi.hundsun.oxchains.base.core.service.system.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.mapper.system.RoleMapper;
import com.mi.hundsun.oxchains.base.core.mapper.system.RoleMenuMapper;
import com.mi.hundsun.oxchains.base.core.po.system.Role;
import com.mi.hundsun.oxchains.base.core.po.system.RoleMenu;
import com.mi.hundsun.oxchains.base.core.service.system.RoleService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 系统角色表业务相关Service接口实现<br>
 *
 * @author donfy
 * @ClassName: RoleServiceImpl
 * @date 2017-08-21 09:50:59
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public Role saveRole(Role role) throws BussinessException {
        role.setDelFlag(Role.DELFLAG.NO.code);
        role.setCreateTime(new Date());
        int i = roleMapper.insert(role);
        if (i != 1) throw new BussinessException("保存失败！");
        return role;
    }

    @Override
    public void deleteRole(Role role) throws BussinessException {
        role.setDelFlag(Role.DELFLAG.YES.code);
        roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public void updateRole(Role role) throws BussinessException {
        roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public Role getRoleById(Integer id) throws BussinessException {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void saveOrUpdateRoleMenu(Integer id, String name, List<Integer> menuIdList) {

        Role role;
        if (id == null || id == 0) {
            // 保存角色信息
            role = roleMapper.selectOne(new Role(r -> {
                r.setName(name);
                r.setDelFlag(Role.DELFLAG.NO.code);
            }));
            if (role!=null){
                throw new BussinessException("改角色已存在");
            }
            role = new Role();
            role.setName(name);
            roleMapper.insert(role);

            role = roleMapper.selectOne(new Role(r -> {
                r.setName(name);
                r.setDelFlag(Role.DELFLAG.NO.code);
            }));
        } else {
            // 修改 角色信息
            role = roleMapper.selectOne(new Role(r -> {
                r.setId(id);
                r.setDelFlag(Role.DELFLAG.NO.code);
            }));
            if (role == null) {
                throw new BussinessException("保存角色信息失败，角色不存在");
            }
            role.setName(name);
            roleMapper.updateByPrimaryKeySelective(role);
            // 删除角色菜单关联关系
            roleMenuMapper.deleteByRoleId(role.getId());
        }

        // 保存角色菜单关联关系
        for (Integer menuId : menuIdList) {
            RoleMenu rm = new RoleMenu();
            rm.setRoleId(role.getId());
            rm.setMenuId(menuId);
            rm.setCreateTime(new Date());
            rm.setDelFlag(RoleMenu.DELFLAG.NO.code);
            roleMenuMapper.insert(rm);
        }
    }

    @Override
    public void deleteRoleById(Integer id) {
        // 删除角色信息
        roleMapper.deleteByPrimaryKey(id);
        // 删除角色菜单关联关系
        roleMenuMapper.deleteByRoleId(id);
    }

    @Override
    public GenericMapper<Role, Integer> _getMapper() {
        return roleMapper;
    }

}
