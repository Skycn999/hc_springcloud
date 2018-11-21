package com.mi.hundsun.oxchains.base.core.service.user.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.mapper.user.UserFreezeMapper;
import com.mi.hundsun.oxchains.base.core.po.user.UserFreeze;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.base.core.service.user.UserFreezeService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class UserFreezeServiceImpl implements UserFreezeService {

    @Resource
    private UserFreezeMapper userFreezeMapper;

    @Resource
    private RedisService redisService;

    @Override
    public GenericMapper<UserFreeze,Integer> _getMapper() {
        return userFreezeMapper;
    }
}
