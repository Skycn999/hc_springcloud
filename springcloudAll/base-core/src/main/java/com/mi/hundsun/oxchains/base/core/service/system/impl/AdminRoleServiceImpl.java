package com.mi.hundsun.oxchains.base.core.service.system.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.system.AdminRoleMapper;
import com.mi.hundsun.oxchains.base.core.po.system.AdminRole;
import com.mi.hundsun.oxchains.base.core.service.system.AdminRoleService;

/**
 * 管理员与角色关联业务相关Service接口实现<br>
 *
 * @ClassName: AdminRoleServiceImpl
 * @author bin
 * @date   2018-04-11 05:57:01
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class AdminRoleServiceImpl implements AdminRoleService {

	@Resource
    private AdminRoleMapper adminRoleMapper;

    @Override
    public GenericMapper<AdminRole,Integer> _getMapper() {
        return adminRoleMapper;
    }

}
