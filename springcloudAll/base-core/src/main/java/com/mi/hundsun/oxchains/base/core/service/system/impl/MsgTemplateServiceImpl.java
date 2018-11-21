package com.mi.hundsun.oxchains.base.core.service.system.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.mapper.system.MsgTemplateMapper;
import com.mi.hundsun.oxchains.base.core.po.system.MsgTemplate;
import com.mi.hundsun.oxchains.base.core.service.system.MsgTemplateService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 消息模板表业务相关Service接口实现<br>
 *
 * @ClassName: MessageTemplateServiceImpl
 * @author donfy
 * @date   2017-08-16 01:52:00
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class MsgTemplateServiceImpl implements MsgTemplateService {

	@Resource
    private MsgTemplateMapper msgTemplateMapper;

    @Override
    public GenericMapper<MsgTemplate,Integer> _getMapper() {
        return msgTemplateMapper;
    }

}
