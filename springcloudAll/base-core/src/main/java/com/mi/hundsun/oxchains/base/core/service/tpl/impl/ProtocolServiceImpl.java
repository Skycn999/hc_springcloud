package com.mi.hundsun.oxchains.base.core.service.tpl.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.tpl.ProtocolMapper;
import com.mi.hundsun.oxchains.base.core.po.tpl.Protocol;
import com.mi.hundsun.oxchains.base.core.service.tpl.ProtocolService;

/**
 * 各种协议模板业务相关Service接口实现<br>
 *
 * @ClassName: ProtocolServiceImpl
 * @author bin
 * @date   2018-04-23 09:50:40
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class ProtocolServiceImpl implements ProtocolService {

	@Resource
    private ProtocolMapper protocolMapper;

    @Override
    public GenericMapper<Protocol,Integer> _getMapper() {
        return protocolMapper;
    }

}
