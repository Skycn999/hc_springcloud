package com.mi.hundsun.oxchains.base.core.service.tpl.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.tpl.PercentControlMapper;
import com.mi.hundsun.oxchains.base.core.po.tpl.PercentControl;
import com.mi.hundsun.oxchains.base.core.service.tpl.PercentControlService;

/**
 * 百分比风控模板业务相关Service接口实现<br>
 *
 * @ClassName: PercentControlServiceImpl
 * @author bin
 * @date   2018-04-13 10:28:11
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class PercentControlServiceImpl implements PercentControlService {

	@Resource
    private PercentControlMapper percentControlMapper;

    @Override
    public GenericMapper<PercentControl,Integer> _getMapper() {
        return percentControlMapper;
    }

}
