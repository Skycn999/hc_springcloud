package com.mi.hundsun.oxchains.base.core.service.user.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.mapper.user.UserAttachmentMapper;
import com.mi.hundsun.oxchains.base.core.po.user.UserAttachment;
import com.mi.hundsun.oxchains.base.core.service.user.UserAttachmentService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class UserAttachmentServiceImpl implements UserAttachmentService {

    @Resource
    private UserAttachmentMapper userAttachmentMapper;

    @Override
    public GenericMapper<UserAttachment,Integer> _getMapper() {
        return userAttachmentMapper;
    }

}
