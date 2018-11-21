package com.mi.hundsun.oxchains.base.core.service.user.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.mapper.user.UserIdentifyMapper;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.base.core.service.user.UserIdentifyService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用户认证业务相关Service接口实现<br>
 *
 * @ClassName: UserIdentifyServiceImpl
 * @author lzj
 * @date   2018-04-12 03:40:50
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class UserIdentifyServiceImpl implements UserIdentifyService {

	@Resource
    private UserIdentifyMapper userIdentifyMapper;

    @Override
    public GenericMapper<UserIdentify,Integer> _getMapper() {
        return userIdentifyMapper;
    }
}
