package com.mi.hundsun.oxchains.base.core.service.system.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.mapper.system.AdminMapper;
import com.mi.hundsun.oxchains.base.core.mapper.system.RoleMapper;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.base.core.po.system.Role;
import com.mi.hundsun.oxchains.base.core.service.system.AdminService;
import com.xiaoleilu.hutool.crypto.digest.DigestUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 管理员账号业务相关Service接口实现<br>
 *
 * @author donfy
 * @ClassName: AdminServiceImpl
 * @date 2017-08-16 08:18:00
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private RoleMapper roleMapper;

//    @Override
//    public Admin saveAdmin(Admin admin) throws BussinessException {
//        if (admin.getType()==null){
//            throw new BussinessException("请选择管理员类型");
//        }
//        if (admin.getType()== Admin.TYPE.T1.code){
//            admin.setBussId(null);
//        }else if(admin.getType()== Admin.TYPE.T2.code||admin.getType()== Admin.TYPE.T3.code){
//            if(admin.getBussId()==null){
//                throw new BussinessException("请选择绑定人");
//            }
////            List<Admin> adminList =  adminMapper.select(new Admin(a->{
////                a.setBussId(admin.getBussId());
////                a.setType(admin.getType());
////                a.setDelFlag(Admin.DELFLAG.NORMAL.code);
////            }));
////            if(adminList.size()>0){
////                throw new BussinessException("选择的用户已绑定");
////            }
//        }
//        Integer roleId = admin.getRoleId();
//        if (roleId == null || roleId <= 0) {
//            throw new BussinessException("请选择管理员角色");
//        }
//        Role role = roleMapper.selectByPrimaryKey(roleId);
//        if (role == null) {
//            throw new BussinessException("您选择的角色不存在或已删除，请重新选择");
//        }
//        admin.setRoleName(role.getName());
//        admin.setPassword(DigestUtil.md5Hex(admin.getPassword()));
//        admin.setCreateTime(new Date());
//        admin.setDelFlag(Admin.DELFLAG.NORMAL.code);
//        int i = adminMapper.insert(admin);
//        if (i != 1) throw new BussinessException("保存失败!");
//        return admin;
//    }

//    @Override
//    public void updateAdmin(Admin admin) throws BussinessException {
//        Integer roleId = admin.getRoleId();
//        if (roleId == null || roleId <= 0) {
//            throw new BussinessException("请选择管理员角色");
//        }
//        Role role = roleMapper.selectByPrimaryKey(roleId);
//        if (role == null) {
//            throw new BussinessException("您选择的角色不存在或已删除，请重新选择");
//        }
//        admin.setRoleName(role.getName());
//        admin.setPassword(DigestUtil.md5Hex(admin.getPassword()));
//        this.updateByPrimaryKeySelective(admin);
//    }

    @Override
    public Admin getAdminById(Integer id) throws BussinessException {
        return this.selectByPrimaryKey(id);
    }

    @Override
    public GenericMapper<Admin, Integer> _getMapper() {
        return adminMapper;
    }

}
